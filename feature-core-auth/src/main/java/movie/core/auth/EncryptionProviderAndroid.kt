package movie.core.auth

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import javax.crypto.Cipher

class EncryptionProviderAndroid(
    private val context: Context,
    alias: String = context.packageName
) : EncryptionProvider {

    private val keys by lazy {
        val keystore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        keystore.getOrCreatePrivateKey(alias)
    }
    private val cipher by lazy { Cipher.getInstance("RSA/ECB/OAEPwithSHA-1andMGF1Padding") }

    override fun encrypt(value: String): Result<String> {
        val cipher = cipher
        cipher.init(Cipher.ENCRYPT_MODE, keys.public)
        val result = cipher.doFinal(value.encodeToByteArray())
        val encoded = Base64.encodeToString(result, Base64.URL_SAFE or Base64.NO_PADDING)
        return Result.success(encoded)
    }

    override fun decrypt(value: String): Result<String> {
        val cipher = cipher
        cipher.init(Cipher.DECRYPT_MODE, keys.private)
        val bytes = Base64.decode(value.encodeToByteArray(), Base64.URL_SAFE or Base64.NO_PADDING)
        val result = cipher.doFinal(bytes)
        val decoded = result.decodeToString()
        return Result.success(decoded)
    }

    private fun KeyStore.getOrCreatePrivateKey(alias: String): KeyPair {
        val key = getEntry(alias, null)
        if (key is KeyStore.PrivateKeyEntry)
            return KeyPair(key.certificate.publicKey, key.privateKey)
        val generator =
            KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore")
        val purpose = KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        val spec = KeyGenParameterSpec.Builder(alias, purpose)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
            .setDigests(KeyProperties.DIGEST_SHA1)
            .build()
        generator.initialize(spec)
        return generator.generateKeyPair()
    }

}