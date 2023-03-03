package movie.core.db.dao

import movie.core.db.io
import movie.core.db.model.MovieDetailStored

class MovieDetailDaoThreading(
    private val origin: MovieDetailDao
) : MovieDetailDao {

    override suspend fun insert(model: MovieDetailStored) = origin.io { insert(model) }

    override suspend fun delete(model: MovieDetailStored) = origin.io { delete(model) }

    override suspend fun update(model: MovieDetailStored) = origin.io { update(model) }

    override suspend fun select(id: String) = origin.io { select(id) }

}