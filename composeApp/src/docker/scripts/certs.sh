#!/bin/bash

set -euo pipefail

# Define base directories
BASE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)/docker"
CRYPTO_CONFIG_DIR="$BASE_DIR/crypto-config"

# Define CA details
declare -A CA_DETAILS=(
  ["org1"]="7054"
  ["org2"]="8054"
  ["orderer"]="9054"
)

# Define identities to register and enroll
declare -A IDENTITIES=(
  ["org1.peer0"]="peer0:peer0pw"
  ["org2.peer0"]="peer0:peer0pw"
  ["orderer.orderer"]="orderer:ordererpw"
)

# Function to enroll CA admin
enroll_ca_admin() {
  local org=$1
  local port=${CA_DETAILS[$org]}
  local ca_name="ca-${org}"
  local ca_cert="$CRYPTO_CONFIG_DIR/fabric-ca/${org}/tls-cert.pem"
  local msp_dir="$CRYPTO_CONFIG_DIR/peerOrganizations/${org}.votechain.com"

  export FABRIC_CA_CLIENT_HOME=$msp_dir

  fabric-ca-client enroll \
    -u https://admin:adminpw@localhost:"${port}" \
    --caname "$ca_name" \
    --tls.certfiles "$ca_cert"
}

# Function to register and enroll identities
register_and_enroll() {
  local org=$1
  local name=$2
  local secret=$3
  local port=${CA_DETAILS[$org]}
  local ca_name="ca-${org}"
  local ca_cert="$CRYPTO_CONFIG_DIR/fabric-ca/${org}/tls-cert.pem"
  local msp_dir="$CRYPTO_CONFIG_DIR/peerOrganizations/${org}.votechain.com"

  export FABRIC_CA_CLIENT_HOME=$msp_dir

  # Register identity
  fabric-ca-client register \
    --caname $ca_name \
    --id.name $name \
    --id.secret $secret \
    --id.type peer \
    --tls.certfiles $ca_cert

  # Enroll identity
  fabric-ca-client enroll \
    -u https://$name:$secret@localhost:${port} \
    --caname $ca_name \
    --csr.hosts $name.${org}.votechain.com \
    --tls.certfiles $ca_cert \
    -M $msp_dir/peers/$name.${org}.votechain.com/msp

  # Enroll TLS
  fabric-ca-client enroll \
    -u https://$name:$secret@localhost:${port} \
    --caname $ca_name \
    --enrollment.profile tls \
    --csr.hosts $name.${org}.votechain.com \
    --tls.certfiles $ca_cert \
    -M $msp_dir/peers/$name.${org}.votechain.com/tls

  # Copy TLS files to standard locations
  cp $msp_dir/peers/$name.${org}.votechain.com/tls/tlscacerts/* $msp_dir/peers/$name.${org}.votechain.com/tls/ca.crt
  cp $msp_dir/peers/$name.${org}.votechain.com/tls/signcerts/* $msp_dir/peers/$name.${org}.votechain.com/tls/server.crt
  cp $msp_dir/peers/$name.${org}.votechain.com/tls/keystore/* $msp_dir/peers/$name.${org}.votechain.com/tls/server.key
}

# Main execution
for org in "${!CA_DETAILS[@]}"; do
  echo "Enrolling CA admin for $org"
  enroll_ca_admin "$org"
done

for key in "${!IDENTITIES[@]}"; do
  IFS='.' read -r org identity <<< "$key"
  IFS=':' read -r name secret <<< "${IDENTITIES[$key]}"
  echo "Registering and enrolling $name for $org"
  register_and_enroll "$org" "$name" "$secret"
done
