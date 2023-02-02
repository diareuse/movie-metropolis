package movie.rating

interface RatingProvider {

    suspend fun get(descriptor: MovieDescriptor): AvailableRating?

    interface Composed {

        suspend fun get(vararg descriptors: MovieDescriptor): ComposedRating

    }

}