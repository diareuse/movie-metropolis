package movie.core

import movie.core.adapter.MoviePromoPosterWithUrl
import movie.core.model.Movie
import movie.core.model.MoviePromoPoster
import movie.core.nwk.EndpointProvider

class EventPromoFeatureUrlWrap(
    private val origin: EventPromoFeature,
    private val provider: EndpointProvider
) : EventPromoFeature {

    override suspend fun get(movie: Movie): Result<MoviePromoPoster> {
        return origin.get(movie).map {
            MoviePromoPosterWithUrl(it, provider)
        }
    }

}