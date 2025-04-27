package com.trivaris.votechain.crypto

import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.cert.X509v3CertificateBuilder
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import org.hyperledger.fabric.gateway.Identities
import org.hyperledger.fabric.gateway.Identity
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.Security
import java.security.cert.X509Certificate
import java.util.Date

class IdentityCreator {
    init {
        Security.addProvider(BouncyCastleProvider())
    }

    fun makeIdentity(): Identity {
        val keyPairGen = KeyPairGenerator.getInstance("EC")
        keyPairGen.initialize(256)
        val keyPair = keyPairGen.generateKeyPair()

        val now = Date()
        val expiry = Date(now.time + 365 * 24 * 60 * 60 * 1000L)
        val dn = X500Name("CN=Anonymous User,O=Org1,L=Nowhere,C=ZZ")
        val certBuilder: X509v3CertificateBuilder = JcaX509v3CertificateBuilder(
            dn,
            BigInteger.valueOf(System.currentTimeMillis()),
            now,
            expiry,
            dn,
            keyPair.public
        )

        val contentSigner = JcaContentSignerBuilder("SHA256withECDSA")
            .setProvider("BC")
            .build(keyPair.private)

        val certHolder = certBuilder.build(contentSigner)

        val certificate: X509Certificate = JcaX509CertificateConverter()
            .setProvider("BC")
            .getCertificate(certHolder)

        val identity = Identities.newX509Identity("Org1MSP", certificate, keyPair.private)

        return identity
    }
}