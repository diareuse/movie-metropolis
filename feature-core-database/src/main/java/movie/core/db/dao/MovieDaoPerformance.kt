package movie.core.db.dao

import movie.core.db.PerformanceTracer
import movie.core.db.model.MovieStored
import movie.core.db.trace

class MovieDaoPerformance(
    private val origin: MovieDao,
    private val tracer: PerformanceTracer
) : MovieDao {

    override suspend fun insert(model: MovieStored) = tracer.trace("$Tag.insert") {
        origin.insert(model)
    }

    override suspend fun delete(model: MovieStored) = tracer.trace("$Tag.delete") {
        origin.delete(model)
    }

    override suspend fun update(model: MovieStored) = tracer.trace("$Tag.update") {
        origin.update(model)
    }

    override suspend fun select(id: String) = tracer.trace("$Tag.select") {
        origin.select(id)
    }

    companion object {
        private const val Tag = "movie"
    }

}