package movie.core

import movie.core.model.Movie
import movie.core.model.MovieDetail

interface EventDetailFeature {

    @Throws(Throwable::class)
    suspend fun get(movie: Movie): MovieDetail

}