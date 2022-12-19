package movie.rating

interface RatingProvider {

    suspend fun getRating(descriptor: MovieDescriptor): Byte

}

