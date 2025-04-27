#!/bin/bash

set -e

cryptogen generate --config=../crypto-config.yaml --output=../crypto-config
configtxgen -configPath=../config -profile VoteChainOrdererGenesis -channelID system-channel -outputBlock ./config/genesis.block
configtxgen -configPath=../config -profile VoteChainChannel -outputCreateChannelTx ./config/channel.tx -channelID votechain