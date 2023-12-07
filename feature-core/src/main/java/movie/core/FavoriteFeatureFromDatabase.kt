package movie.core

import movie.core.adapter.MovieFavoriteFromDatabase
import movie.core.adapter.MovieFromDatabase
import movie.core.db.dao.MovieDao
import movie.core.db.dao.MovieFavoriteDao
import movie.core.db.model.MovieFavoriteStored
import movie.core.model.Movie
import movie.core.model.MovieFavorite
import java.util.Date

class FavoriteFeatureFromDatabase(
    private val favoriteDao: MovieFavoriteDao,
    private val movieDao: MovieDao
) : FavoriteFeature {

    override suspend fun isFavorite(movie: Movie) =
        favoriteDao.isFavorite(movie.id)

    override suspend fun setNotified(movie: Movie) =
        favoriteDao.setFavorite(movie.id)

    override suspend fun toggle(movie: Movie): Boolean {
        val favorite = MovieFavoriteStored(movie = movie.id, movie.releasedAt.before(Date()))
        val isFavorite = isFavorite(movie)
        when (isFavorite) {
            true -> favoriteDao.delete(favorite)
            else -> favoriteDao.insertOrUpdate(favorite)
        }
        return !isFavorite
    }

    override suspend fun getPending() = favoriteDao.selectPending()
        .mapNotNull {
            movieDao.select(it.movie)?.let(::MovieFromDatabase)
                ?.let { m -> MovieFavoriteFromDatabase(it, m) }
        }

    override suspend fun getAll() = favoriteDao.selectAll()
        .mapNotNull {
            movieDao.select(it.movie)?.let(::MovieFromDatabase)
                ?.let { m -> MovieFavoriteFromDatabase(it, m) }
        }

    override suspend fun get(movie: Movie): MovieFavorite {
        val favorite = favoriteDao.select(movie.id).let(::requireNotNull)
        val movie = favorite.let { movieDao.select(it.movie) }?.let(::MovieFromDatabase)
            .let(::requireNotNull)
        return MovieFavoriteFromDatabase(favorite, movie)
    }
}