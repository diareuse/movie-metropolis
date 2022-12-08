package movie.core.db.dao

import movie.core.db.model.ShowingStored

class ShowingDaoLowercase(
    private val origin: ShowingDao
) : ShowingDao by origin {

    override suspend fun insert(model: ShowingStored) {
        return origin.insert(model.lowercase())
    }

    override suspend fun delete(model: ShowingStored) {
        return origin.delete(model.lowercase())
    }

    override suspend fun update(model: ShowingStored) {
        return origin.update(model.lowercase())
    }

    override suspend fun selectByCinema(
        rangeStart: Long,
        rangeEnd: Long,
        cinema: String
    ): List<ShowingStored> {
        return origin.selectByCinema(rangeStart, rangeEnd, cinema.lowercase())
    }

    // ---

    private fun ShowingStored.lowercase() = copy(
        id = id.lowercase(),
        cinema = cinema.lowercase(),
        movie = movie.lowercase()
    )

}