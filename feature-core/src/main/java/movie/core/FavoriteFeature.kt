package movie.core

import movie.core.model.Movie
import movie.core.model.MoviePreview

interface FavoriteFeature {

    suspend fun isFavorite(movie: Movie): Result<Boolean>
    suspend fun toggle(movie: Movie): Result<Unit>
    suspend fun getAll(): Result<List<MoviePreview>>

}

