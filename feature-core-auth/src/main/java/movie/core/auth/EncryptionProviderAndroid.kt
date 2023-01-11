package movie.core.auth

import android.content.Context
import android.util.Base64

class EncryptionProviderAndroid(
    context: Context,
    keySpec: CipherFactory.KeySpec,
    alias: String = context.packageName
) : EncryptionProvider {

    private val cipher = CipherFactory(alias, keySpec)

    override fun encrypt(value: String): Result<String> {
        val cipher = cipher.getEncryptCipher()
        val result = cipher.doFinal(value.encodeToByteArray())
        val encoded = Base64.encodeToString(result, Base64.URL_SAFE or Base64.NO_PADDING)
        return Result.success(encoded)
    }

    override fun decrypt(value: String): Result<String> {
        val cipher = cipher.getDecryptCipher()
        val bytes = Base64.decode(value.encodeToByteArray(), Base64.URL_SAFE or Base64.NO_PADDING)
        val result = cipher.doFinal(bytes)
        val decoded = result.decodeToString()
        return Result.success(decoded)
    }

}