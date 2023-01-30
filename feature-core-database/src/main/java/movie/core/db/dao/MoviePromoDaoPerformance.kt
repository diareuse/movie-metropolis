package movie.core.db.dao

import movie.core.db.PerformanceTracer
import movie.core.db.model.MoviePromoStored
import movie.core.db.trace

class MoviePromoDaoPerformance(
    private val origin: MoviePromoDao,
    private val tracer: PerformanceTracer
) : MoviePromoDao {

    override suspend fun insert(model: MoviePromoStored) = tracer.trace("$Tag.insert") {
        origin.insert(model)
    }

    override suspend fun delete(model: MoviePromoStored) = tracer.trace("$Tag.delete") {
        origin.delete(model)
    }

    override suspend fun update(model: MoviePromoStored) = tracer.trace("$Tag.update") {
        origin.update(model)
    }

    override suspend fun select(id: String): String? = tracer.trace("$Tag.select") {
        origin.select(id)
    }

    companion object {

        private const val Tag = "movie-promo"

    }

}