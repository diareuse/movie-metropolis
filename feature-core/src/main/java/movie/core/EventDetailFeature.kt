package movie.core

import movie.core.model.Movie
import movie.core.model.MovieDetail

interface EventDetailFeature {

    suspend fun get(movie: Movie, result: ResultCallback<MovieDetail>)

    companion object {

        suspend fun EventDetailFeature.get(movie: Movie): Result<MovieDetail> {
            var out: Result<MovieDetail> = Result.failure(IllegalStateException())
            get(movie) {
                out = it
            }
            return out
        }

    }

}