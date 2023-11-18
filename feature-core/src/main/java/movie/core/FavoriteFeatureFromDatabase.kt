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

    override suspend fun isFavorite(movie: Movie): Result<Boolean> = favoriteDao.runCatching {
        isFavorite(movie.id)
    }

    override suspend fun setNotified(movie: Movie): Result<Unit> {
        return favoriteDao.runCatching { setFavorite(movie.id) }
    }

    override suspend fun toggle(movie: Movie): Result<Boolean> = favoriteDao.runCatching {
        val favorite = MovieFavoriteStored(movie = movie.id, movie.releasedAt.before(Date()))
        val isFavorite = isFavorite(movie).getOrThrow()
        when (isFavorite) {
            true -> delete(favorite)
            else -> insertOrUpdate(favorite)
        }
        !isFavorite
    }

    override suspend fun getPending(): Result<List<MovieFavorite>> = favoriteDao.runCatching {
        selectPending().mapNotNull {
            val movie = movieDao.select(it.movie)?.let(::MovieFromDatabase)
                ?: return@mapNotNull null
            MovieFavoriteFromDatabase(it, movie)
        }
    }

    override suspend fun getAll() = runCatching {
        favoriteDao.selectAll().mapNotNull {
            val movie = movieDao.select(it.movie)?.let(::MovieFromDatabase)
                ?: return@mapNotNull null
            MovieFavoriteFromDatabase(it, movie)
        }
    }

    override suspend fun get(movie: Movie): Result<MovieFavorite> {
        return favoriteDao.runCatching { select(movie.id).let(::requireNotNull) }.mapCatching {
            println("$movieDao, ${it.movie}")
            val movie = movieDao.select(it.movie)?.let(::MovieFromDatabase).let(::requireNotNull)
            MovieFavoriteFromDatabase(it, movie)
        }
    }
}