package movie.core.db.dao

import movie.core.db.PerformanceTracer
import movie.core.db.model.ShowingStored
import movie.core.db.trace

class ShowingDaoPerformance(
    private val origin: ShowingDao,
    private val tracer: PerformanceTracer
) : ShowingDao {

    override suspend fun insert(model: ShowingStored) = tracer.trace("$Tag.insert") {
        origin.insert(model)
    }

    override suspend fun delete(model: ShowingStored) = tracer.trace("$Tag.delete") {
        origin.delete(model)
    }

    override suspend fun update(model: ShowingStored) = tracer.trace("$Tag.update") {
        origin.update(model)
    }

    override suspend fun selectByCinema(
        rangeStart: Long,
        rangeEnd: Long,
        cinema: String,
        movie: String
    ): List<ShowingStored> = tracer.trace("$Tag.select-by-cinema-movie") {
        origin.selectByCinema(rangeStart, rangeEnd, cinema, movie)
    }

    override suspend fun selectByCinema(
        rangeStart: Long,
        rangeEnd: Long,
        cinema: String
    ): List<ShowingStored> = tracer.trace("$Tag.select-by-cinema") {
        origin.selectByCinema(rangeStart, rangeEnd, cinema)
    }

    companion object {
        private const val Tag = "showings"
    }

}