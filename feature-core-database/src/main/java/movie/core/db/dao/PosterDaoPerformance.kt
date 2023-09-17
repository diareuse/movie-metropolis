package movie.core.db.dao

import movie.core.db.PerformanceTracer
import movie.core.db.trace

class PosterDaoPerformance(
    private val origin: PosterDao,
    private val tracer: PerformanceTracer
) : PosterDao {

    override suspend fun selectAll() = tracer.trace("$Tag.selectAll") {
        origin.selectAll()
    }

    companion object {
        private const val Tag = "posters"
    }

}