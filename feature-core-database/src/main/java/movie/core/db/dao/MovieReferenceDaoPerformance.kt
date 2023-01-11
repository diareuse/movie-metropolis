package movie.core.db.dao

import movie.core.db.PerformanceTracer
import movie.core.db.model.MovieReferenceStored
import movie.core.db.trace

class MovieReferenceDaoPerformance(
    private val origin: MovieReferenceDao,
    private val tracer: PerformanceTracer
) : MovieReferenceDao {

    override suspend fun insert(model: MovieReferenceStored) = tracer.trace("$Tag.insert") {
        origin.insert(model)
    }

    override suspend fun delete(model: MovieReferenceStored) = tracer.trace("$Tag.delete") {
        origin.delete(model)
    }

    override suspend fun update(model: MovieReferenceStored) = tracer.trace("$Tag.update") {
        origin.update(model)
    }

    override suspend fun select(id: String) = tracer.trace("$Tag.select") {
        origin.select(id)
    }

    companion object {
        private const val Tag = "movie-ref"
    }

}