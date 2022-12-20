package movie.rating

interface RatingProvider {

    suspend fun getRating(descriptor: MovieDescriptor): Byte

}

suspend fun RatingProvider.getRating(vararg descriptors: MovieDescriptor) = descriptors
    .distinct()
    .firstNotNullOfOrNull { getRating(it).takeUnless { it == 0.toByte() } } ?: 0