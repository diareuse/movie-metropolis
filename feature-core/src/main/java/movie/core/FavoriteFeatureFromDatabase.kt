package movie.core

import movie.core.db.dao.MovieFavoriteDao
import movie.core.db.dao.MovieMediaDao
import movie.core.db.model.MovieFavoriteStored
import movie.core.model.Movie
import movie.core.model.MoviePreview

class FavoriteFeatureFromDatabase(
    private val favoriteDao: MovieFavoriteDao,
    private val mediaDao: MovieMediaDao
) : FavoriteFeature {

    override suspend fun isFavorite(movie: Movie): Result<Boolean> =
        favoriteDao.runCatching {
            isFavorite(movie.id)
        }

    override suspend fun toggle(movie: MoviePreview): Result<Boolean> =
        favoriteDao.runCatching {
            val favorite = MovieFavoriteStored(movie = movie.id)
            val isFavorite = isFavorite(movie).getOrThrow()
            when (isFavorite) {
                true -> delete(favorite)
                else -> insertOrUpdate(favorite)
            }
            !isFavorite
        }

    override suspend fun getAll() = runCatching {
        favoriteDao.selectAll().map { MoviePreviewFromDatabase(it, mediaDao.select(it.id)) }
    }

}