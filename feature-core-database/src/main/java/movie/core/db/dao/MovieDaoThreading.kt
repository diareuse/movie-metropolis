package movie.core.db.dao

import movie.core.db.io
import movie.core.db.model.MovieStored

class MovieDaoThreading(
    private val origin: MovieDao
) : MovieDao {

    override suspend fun insert(model: MovieStored) = origin.io { insert(model) }

    override suspend fun delete(model: MovieStored) = origin.io { delete(model) }

    override suspend fun update(model: MovieStored) = origin.io { update(model) }

    override suspend fun getDuration(id: String) = origin.io { getDuration(id) }

}