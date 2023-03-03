package movie.core.db.dao

import movie.core.db.io
import movie.core.db.model.MovieMediaStored

class MovieMediaDaoThreading(
    private val origin: MovieMediaDao
) : MovieMediaDao {

    override suspend fun insert(model: MovieMediaStored) = origin.io { insert(model) }

    override suspend fun delete(model: MovieMediaStored) = origin.io { delete(model) }

    override suspend fun update(model: MovieMediaStored) = origin.io { update(model) }

    override suspend fun select(id: String) = origin.io { select(id) }
}