package movie.rating

import movie.rating.internal.AvailableRating
import movie.rating.internal.ComposedRating

interface RatingProvider {

    suspend fun get(descriptor: MovieDescriptor): AvailableRating?

    interface Composed {

        suspend fun get(vararg descriptors: MovieDescriptor): ComposedRating

    }

}