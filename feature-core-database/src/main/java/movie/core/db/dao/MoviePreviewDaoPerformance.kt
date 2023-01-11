package movie.core.db.dao

import movie.core.db.PerformanceTracer
import movie.core.db.model.MoviePreviewStored
import movie.core.db.trace

class MoviePreviewDaoPerformance(
    private val origin: MoviePreviewDao,
    private val tracer: PerformanceTracer
) : MoviePreviewDao {

    override suspend fun insert(model: MoviePreviewStored) = tracer.trace("$Tag.insert") {
        origin.insert(model)
    }

    override suspend fun delete(model: MoviePreviewStored) = tracer.trace("$Tag.delete") {
        origin.delete(model)
    }

    override suspend fun update(model: MoviePreviewStored) = tracer.trace("$Tag.update") {
        origin.update(model)
    }

    override suspend fun selectUpcoming() = tracer.trace("$Tag.upcoming") {
        origin.selectUpcoming()
    }

    override suspend fun selectCurrent() = tracer.trace("$Tag.current") {
        origin.selectCurrent()
    }

    override suspend fun deleteAll(upcoming: Boolean) = tracer.trace("$Tag.delete-all") {
        origin.deleteAll(upcoming)
    }

    companion object {
        private const val Tag = "movie-prev"
    }

}