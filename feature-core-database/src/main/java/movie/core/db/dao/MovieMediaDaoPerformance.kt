package movie.core.db.dao

import movie.core.db.PerformanceTracer
import movie.core.db.model.MovieMediaStored
import movie.core.db.trace

class MovieMediaDaoPerformance(
    private val origin: MovieMediaDao,
    private val tracer: PerformanceTracer
) : MovieMediaDao {

    override suspend fun insert(model: MovieMediaStored) = tracer.trace("$Tag.insert") {
        origin.insert(model)
    }

    override suspend fun delete(model: MovieMediaStored) = tracer.trace("$Tag.delete") {
        origin.delete(model)
    }

    override suspend fun update(model: MovieMediaStored) = tracer.trace("$Tag.update") {
        origin.update(model)
    }

    override suspend fun select(id: String) = tracer.trace("$Tag.select") {
        origin.select(id)
    }

    companion object {
        private const val Tag = "movie-media"
    }

}