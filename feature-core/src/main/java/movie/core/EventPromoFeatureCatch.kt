package movie.core

import movie.core.model.Movie

class EventPromoFeatureCatch(
    private val origin: EventPromoFeature
) : EventPromoFeature, Recoverable {

    override suspend fun get(movie: Movie) = wrap { origin.get(movie) }

}