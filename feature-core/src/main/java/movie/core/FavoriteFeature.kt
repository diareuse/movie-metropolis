package movie.core

import movie.core.model.Movie
import movie.core.model.MovieFavorite

interface FavoriteFeature {

    suspend fun isFavorite(movie: Movie): Boolean
    suspend fun toggle(movie: Movie): Boolean
    suspend fun setNotified(movie: Movie)

    suspend fun getPending(): List<MovieFavorite>
    suspend fun getAll(): List<MovieFavorite>
    suspend fun get(movie: Movie): MovieFavorite

}

