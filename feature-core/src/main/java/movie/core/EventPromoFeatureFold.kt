package movie.core

import movie.core.model.Movie

class EventPromoFeatureFold(
    private vararg val options: EventPromoFeature
) : EventPromoFeature, Recoverable {

    override suspend fun get(movie: Movie) = options.fold { get(movie) }

}