package movie.core.db.dao

import movie.core.db.io
import movie.core.db.model.MoviePreviewStored

class MoviePreviewDaoThreading(
    private val origin: MoviePreviewDao
) : MoviePreviewDao {

    override suspend fun insert(model: MoviePreviewStored) = origin.io { insert(model) }

    override suspend fun delete(model: MoviePreviewStored) = origin.io { delete(model) }

    override suspend fun update(model: MoviePreviewStored) = origin.io { update(model) }

    override suspend fun selectUpcoming() = origin.io { selectUpcoming() }

    override suspend fun selectCurrent() = origin.io { selectCurrent() }

    override suspend fun deleteAll(upcoming: Boolean) = origin.io { deleteAll(upcoming) }

}