package movie.core

import movie.core.model.Movie
import movie.core.model.MovieDetail

interface EventDetailFeature {

    suspend fun get(movie: Movie): Result<MovieDetail>

}