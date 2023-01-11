package movie.core.db.dao

import movie.core.db.PerformanceTracer
import movie.core.db.model.CinemaStored
import movie.core.db.trace

class CinemaDaoPerformance(
    private val origin: CinemaDao,
    private val tracer: PerformanceTracer
) : CinemaDao {

    override suspend fun selectAll() = tracer.trace("$Tag.selectAll") {
        origin.selectAll()
    }

    override suspend fun select(id: String) = tracer.trace("$Tag.select") {
        origin.select(id)
    }

    override suspend fun insert(model: CinemaStored) = tracer.trace("$Tag.insert") {
        origin.insert(model)
    }

    override suspend fun delete(model: CinemaStored) = tracer.trace("$Tag.delete") {
        origin.delete(model)
    }

    override suspend fun update(model: CinemaStored) = tracer.trace("$Tag.update") {
        origin.update(model)
    }

    companion object {
        private const val Tag = "cinema"
    }

}