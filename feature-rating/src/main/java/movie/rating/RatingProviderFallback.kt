package movie.rating

class RatingProviderFallback(
    private vararg val stack: RatingProvider
) : RatingProvider {

    override suspend fun getRating(descriptor: MovieDescriptor): Byte {
        for (provider in stack) try {
            return provider.getRating(descriptor)
        } catch (ignore: Throwable) {
            continue
        }
        return 0
    }

}