package movie.core.db.dao

import movie.core.db.io
import movie.core.db.model.BookingStored

class BookingDaoThreading(
    private val origin: BookingDao
) : BookingDao {

    override suspend fun selectAll() = origin.io { selectAll() }

    override suspend fun selectIds() = origin.io { selectIds() }

    override suspend fun insert(model: BookingStored) = origin.io { insert(model) }

    override suspend fun delete(model: BookingStored) = origin.io { delete(model) }

    override suspend fun update(model: BookingStored) = origin.io { update(model) }

}