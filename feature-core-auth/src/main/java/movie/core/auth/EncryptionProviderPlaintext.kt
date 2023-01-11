package movie.core.auth

import android.util.Base64

class EncryptionProviderPlaintext : EncryptionProvider {

    override fun encrypt(value: String): Result<String> {
        val result = value.encodeToByteArray()
        val encoded = Base64.encodeToString(result, Base64.URL_SAFE or Base64.NO_PADDING)
        return Result.success(encoded)
    }

    override fun decrypt(value: String): Result<String> {
        val result = Base64.decode(value.encodeToByteArray(), Base64.URL_SAFE or Base64.NO_PADDING)
        val decoded = result.decodeToString()
        return Result.success(decoded)
    }

}