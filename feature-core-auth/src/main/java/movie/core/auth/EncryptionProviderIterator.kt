package movie.core.auth

class EncryptionProviderIterator(
    private vararg val providers: EncryptionProvider
) : EncryptionProvider {

    override fun encrypt(value: String): Result<String> {
        for (provider in providers)
            try {
                return provider.encrypt(value)
            } catch (ignore: Throwable) {
                continue
            }
        return Result.failure(IndexOutOfBoundsException("No provider can encrypt successfully"))
    }

    override fun decrypt(value: String): Result<String> {
        for (provider in providers)
            try {
                return provider.decrypt(value)
            } catch (ignore: Throwable) {
                continue
            }
        return Result.failure(IndexOutOfBoundsException("No provider can decrypt successfully"))
    }

}