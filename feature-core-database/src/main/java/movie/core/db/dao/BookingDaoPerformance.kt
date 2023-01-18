package movie.core.db.dao

import movie.core.db.PerformanceTracer
import movie.core.db.model.BookingStored
import movie.core.db.trace

class BookingDaoPerformance(
    private val origin: BookingDao,
    private val tracer: PerformanceTracer
) : BookingDao {

    override suspend fun selectAll() = tracer.trace("$Tag.select-all") {
        origin.selectAll()
    }

    override suspend fun selectIds() = tracer.trace("$Tag.select-ids") {
        origin.selectIds()
    }

    override suspend fun insert(model: BookingStored) = tracer.trace("$Tag.insert") {
        origin.insert(model)
    }

    override suspend fun delete(model: BookingStored) = tracer.trace("$Tag.delete") {
        origin.delete(model)
    }

    override suspend fun update(model: BookingStored) = tracer.trace("$Tag.update") {
        origin.update(model)
    }

    companion object {
        private const val Tag = "booking"
    }

}

