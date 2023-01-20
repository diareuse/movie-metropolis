package movie.core

import movie.core.adapter.MovieDetailFromResponse
import movie.core.model.Movie
import movie.core.model.MovieDetail
import movie.core.nwk.EventService

class EventDetailFeatureNetwork(
    private val service: EventService
) : EventDetailFeature {

    override suspend fun get(movie: Movie, result: ResultCallback<MovieDetail>) {
        val output = service.getDetail(movie.id).getOrThrow()
            .body.details
            .let(::MovieDetailFromResponse)
        result(Result.success(output))
    }

}