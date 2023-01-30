package movie.core

import movie.core.model.Movie
import movie.core.model.MoviePromoPoster

interface EventPromoFeature {

    suspend fun get(movie: Movie, callback: ResultCallback<MoviePromoPoster>)

    companion object {

        suspend fun EventPromoFeature.get(movie: Movie): Result<MoviePromoPoster> {
            var last: Result<MoviePromoPoster> = Result.failure(IllegalStateException())
            get(movie) { last = it }
            return last
        }

    }

}