package movie.core

import movie.core.model.Movie
import movie.core.model.MovieFavorite

interface FavoriteFeature {

    suspend fun isFavorite(movie: Movie): Result<Boolean>
    suspend fun toggle(movie: Movie): Result<Boolean>
    suspend fun setNotified(movie: Movie): Result<Unit>

    suspend fun getPending(): Result<List<MovieFavorite>>
    suspend fun getAll(): Result<List<MovieFavorite>>
    suspend fun get(movie: Movie): Result<MovieFavorite>

}

