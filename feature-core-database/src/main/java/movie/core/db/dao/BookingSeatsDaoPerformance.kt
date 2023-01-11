package movie.core.db.dao

import movie.core.db.PerformanceTracer
import movie.core.db.model.BookingSeatsStored
import movie.core.db.trace

class BookingSeatsDaoPerformance(
    private val origin: BookingSeatsDao,
    private val tracer: PerformanceTracer
) : BookingSeatsDao {

    override suspend fun select(id: String) = tracer.trace("$Tag.select") {
        origin.select(id)
    }

    override suspend fun deleteFor(id: String) = tracer.trace("$Tag.delete-parent") {
        origin.deleteFor(id)
    }

    override suspend fun insert(model: BookingSeatsStored) = tracer.trace("$Tag.insert") {
        origin.insert(model)
    }

    override suspend fun delete(model: BookingSeatsStored) = tracer.trace("$Tag.delete") {
        origin.delete(model)
    }

    override suspend fun update(model: BookingSeatsStored) = tracer.trace("$Tag.update") {
        origin.update(model)
    }

    companion object {
        private const val Tag = "booking-seats"
    }

}