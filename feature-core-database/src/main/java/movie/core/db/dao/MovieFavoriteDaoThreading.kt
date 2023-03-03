package movie.core.db.dao

import movie.core.db.io
import movie.core.db.model.MovieFavoriteStored
import movie.core.db.model.MoviePreviewView

class MovieFavoriteDaoThreading(
    private val origin: MovieFavoriteDao
) : MovieFavoriteDao {

    override suspend fun insert(model: MovieFavoriteStored) = origin.io { insert(model) }

    override suspend fun delete(model: MovieFavoriteStored) = origin.io { delete(model) }

    override suspend fun update(model: MovieFavoriteStored) = origin.io { update(model) }

    override suspend fun selectAll(): List<MoviePreviewView> = origin.io { selectAll() }

    override suspend fun isFavorite(id: String) = origin.io { isFavorite(id) }

}