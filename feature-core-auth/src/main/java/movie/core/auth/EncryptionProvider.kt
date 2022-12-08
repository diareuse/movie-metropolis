package movie.core.auth

interface EncryptionProvider {

    fun encrypt(value: String): Result<String>
    fun decrypt(value: String): Result<String>

}