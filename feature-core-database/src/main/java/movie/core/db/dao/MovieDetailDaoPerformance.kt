package movie.core.db.dao

import movie.core.db.PerformanceTracer
import movie.core.db.model.MovieDetailStored
import movie.core.db.trace

class MovieDetailDaoPerformance(
    private val origin: MovieDetailDao,
    private val tracer: PerformanceTracer
) : MovieDetailDao {

    override suspend fun insert(model: MovieDetailStored) = tracer.trace("$Tag.insert") {
        origin.insert(model)
    }

    override suspend fun delete(model: MovieDetailStored) = tracer.trace("$Tag.delete") {
        origin.delete(model)
    }

    override suspend fun update(model: MovieDetailStored) = tracer.trace("$Tag.update") {
        origin.update(model)
    }

    override suspend fun select(id: String) = tracer.trace("$Tag.select") {
        origin.select(id)
    }

    companion object {
        private const val Tag = "movie-detail"
    }

}