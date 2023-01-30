package movie.core

import movie.core.adapter.MoviePromoPosterWithUrl
import movie.core.model.Movie
import movie.core.model.MoviePromoPoster
import movie.core.nwk.EndpointProvider

class EventPromoFeatureUrlWrap(
    private val origin: EventPromoFeature,
    private val provider: EndpointProvider
) : EventPromoFeature {

    override suspend fun get(movie: Movie, callback: ResultCallback<MoviePromoPoster>) {
        origin.get(movie, callback.map {
            MoviePromoPosterWithUrl(it, provider)
        })
    }

}