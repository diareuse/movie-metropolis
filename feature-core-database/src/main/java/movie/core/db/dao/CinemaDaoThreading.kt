package movie.core.db.dao

import movie.core.db.io
import movie.core.db.model.CinemaStored

class CinemaDaoThreading(
    private val origin: CinemaDao
) : CinemaDao {

    override suspend fun selectAll() = origin.io { selectAll() }

    override suspend fun select(id: String) = origin.io { select(id) }

    override suspend fun insert(model: CinemaStored) = origin.io { insert(model) }

    override suspend fun delete(model: CinemaStored) = origin.io { delete(model) }

    override suspend fun update(model: CinemaStored) = origin.io { update(model) }

}