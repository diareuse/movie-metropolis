package movie.core.db.dao

import movie.core.db.io
import movie.core.db.model.ShowingStored

class ShowingDaoThreading(
    private val origin: ShowingDao
) : ShowingDao {

    override suspend fun insert(model: ShowingStored) = origin.io { insert(model) }

    override suspend fun delete(model: ShowingStored) = origin.io { delete(model) }

    override suspend fun update(model: ShowingStored) = origin.io { update(model) }

    override suspend fun selectByCinema(
        rangeStart: Long,
        rangeEnd: Long,
        cinema: String
    ) = origin.io { selectByCinema(rangeStart, rangeEnd, cinema) }

    override suspend fun selectByCinema(
        rangeStart: Long,
        rangeEnd: Long,
        cinema: String,
        movie: String
    ) = origin.io { selectByCinema(rangeStart, rangeEnd, cinema, movie) }
}