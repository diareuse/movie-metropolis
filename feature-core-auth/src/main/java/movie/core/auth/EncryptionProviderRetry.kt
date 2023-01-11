package movie.core.auth

import java.security.KeyException

class EncryptionProviderRetry(
    private val origin: EncryptionProvider,
    private val times: Int = 3
) : EncryptionProvider {

    override fun encrypt(value: String): Result<String> {
        throw retry {
            return origin.encrypt(value)
        }
    }

    override fun decrypt(value: String): Result<String> {
        throw retry {
            return origin.decrypt(value)
        }
    }

    private inline fun retry(body: () -> Unit): Throwable {
        var last: Throwable = KeyException("Key not found")
        repeat(times) {
            try {
                body()
            } catch (e: Throwable) {
                last = e
            }
        }
        return last
    }

}