package com.trivaris.votechain.common

import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.EncodeHintType
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import com.google.zxing.qrcode.QRCodeWriter
import com.trivaris.votechain.common.Cryptography.ALGORITHM
import java.awt.image.BufferedImage
import java.io.File
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.imageio.ImageIO

fun PublicKey.asString(): String =
    Base64.getEncoder().encodeToString(this.encoded)

fun String.toPublicKey(): PublicKey {
    val publicKeyBytes = Base64.getDecoder().decode(this)
    val keyFactory = KeyFactory.getInstance(ALGORITHM)
    val keySpec = X509EncodedKeySpec(publicKeyBytes)
    return keyFactory.generatePublic(keySpec)
}

fun PrivateKey.asString(): String =
    Base64.getEncoder().encodeToString(this.encoded)

fun String.toPrivateKey(): PrivateKey {
    val privateKeyBytes = Base64.getDecoder().decode(this)
    val keyFactory = KeyFactory.getInstance(ALGORITHM)
    val keySpec = PKCS8EncodedKeySpec(privateKeyBytes)
    return keyFactory.generatePrivate(keySpec)
}

fun String.toQRCode(): BufferedImage {
    val qrWriter = QRCodeWriter()
    val hintMap = mapOf(EncodeHintType.CHARACTER_SET to "UTF-8")
    val bitMatrix = qrWriter.encode(this, BarcodeFormat.QR_CODE, 300, 300, hintMap)

    val width = bitMatrix.width
    val height = bitMatrix.height
    val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    for (x in 0 until width) {
        for (y in 0 until height) {
            bufferedImage.setRGB(x, y, if (bitMatrix.get(x, y)) 0x000000 else 0xFFFFFF)
        }
    }
    return bufferedImage
}

fun BufferedImage.decodeQRCode(): String {
    val luminanceSource = BufferedImageLuminanceSource(this)
    val binaryBitmap = BinaryBitmap(HybridBinarizer(luminanceSource))
    val qrCodeReader = QRCodeReader()
    return qrCodeReader.decode(binaryBitmap).text
}
fun File.decodeQRCode(): String {
    val image = ImageIO.read(this)
    return image.decodeQRCode()
}

fun String.applySha256(): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hash = digest.digest(this.toByteArray(Charsets.UTF_8))
    return hash.joinToString("") { String.format("%02x", it) }
}