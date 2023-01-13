package movie.core.auth

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyException
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.ProviderException
import javax.crypto.Cipher

class CipherFactory(
    alias: String,
    private val keySpec: KeySpec
) {

    private val alias = "$alias.${keySpec.suffix}"
    private val keys
        @Throws(KeyException::class)
        get() = KeyStore.getInstance("AndroidKeyStore").run {
            load(null)
            synchronized(lock) {
                getKeyPair() ?: generateKeyPair(type) ?: throw KeyException("Keystore unavailable")
            }
        }

    private val cipher get() = Cipher.getInstance(keySpec.transformation)

    @Throws(KeyException::class)
    fun getEncryptCipher(): Cipher = cipher.apply {
        init(Cipher.ENCRYPT_MODE, keys.public)
    }

    @Throws(KeyException::class)
    fun getDecryptCipher(): Cipher = cipher.apply {
        init(Cipher.DECRYPT_MODE, keys.private)
    }

    private fun KeyStore.getKeyPair(): KeyPair? {
        val entry = getEntry(alias, null)
        if (entry is KeyStore.PrivateKeyEntry)
            return KeyPair(entry.certificate.publicKey, entry.privateKey)
        return null
    }

    @Throws(KeyException::class)
    private fun generateKeyPair(provider: String): KeyPair? {
        val generator =
            KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, provider)
        val purpose = KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        val specBuilder = KeyGenParameterSpec.Builder(alias, purpose)
            .setDigests(keySpec.digest)
            .setEncryptionPaddings(keySpec.padding)
        if (keySpec.keySize != null)
            specBuilder.setKeySize(keySpec.keySize)
        generator.initialize(specBuilder.build())
        return try {
            generator.generateKeyPair()
        } catch (e: ProviderException) {
            throw KeyException("Generating key of spec $keySpec is not possible", e)
        }
    }

    enum class KeySpec(
        val transformation: String,
        val digest: String,
        val padding: String,
        val keySize: Int?,
        val suffix: String
    ) {
        Leanback(
            transformation = "RSA/ECB/PKCS1Padding",
            digest = KeyProperties.DIGEST_SHA256,
            padding = KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1,
            keySize = 2048,
            suffix = "leanback"
        ),
        Standard(
            transformation = "RSA/ECB/OAEPwithSHA-1andMGF1Padding",
            digest = KeyProperties.DIGEST_SHA1,
            padding = KeyProperties.ENCRYPTION_PADDING_RSA_OAEP,
            keySize = null,
            suffix = "standard"
        )
    }

    companion object {

        private val lock = Any()

    }

}