package movie.core

import movie.core.model.Movie
import movie.core.model.MoviePromoPoster

interface EventPromoFeature {

    suspend fun get(movie: Movie): Result<MoviePromoPoster>

}