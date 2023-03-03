package movie.core.db.dao

import movie.core.db.io
import movie.core.db.model.BookingSeatsStored

class BookingSeatsDaoThreading(
    private val origin: BookingSeatsDao
) : BookingSeatsDao {

    override suspend fun select(id: String) = origin.io { select(id) }

    override suspend fun deleteFor(id: String) = origin.io { deleteFor(id) }

    override suspend fun insert(model: BookingSeatsStored) = origin.io { insert(model) }

    override suspend fun delete(model: BookingSeatsStored) = origin.io { delete(model) }

    override suspend fun update(model: BookingSeatsStored) = origin.io { update(model) }

}