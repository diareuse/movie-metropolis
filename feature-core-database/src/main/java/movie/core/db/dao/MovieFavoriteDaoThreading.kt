package movie.core.db.dao

import movie.core.db.io
import movie.core.db.model.MovieFavoriteStored

class MovieFavoriteDaoThreading(
    private val origin: MovieFavoriteDao
) : MovieFavoriteDao {

    override suspend fun insert(model: MovieFavoriteStored) = origin.io { insert(model) }

    override suspend fun delete(model: MovieFavoriteStored) = origin.io { delete(model) }

    override suspend fun update(model: MovieFavoriteStored) = origin.io { update(model) }

    override suspend fun selectAll() = origin.io { selectAll() }

    override suspend fun selectPending() = origin.io { selectPending() }

    override suspend fun select(id: String) = origin.io { select(id) }

    override suspend fun setFavorite(id: String) = origin.io { setFavorite(id) }

    override suspend fun isFavorite(id: String) = origin.io { isFavorite(id) }

}