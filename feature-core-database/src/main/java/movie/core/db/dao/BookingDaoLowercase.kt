package movie.core.db.dao

import movie.core.db.model.BookingStored

class BookingDaoLowercase(
    private val origin: BookingDao
) : BookingDao by origin {

    override suspend fun insert(model: BookingStored) {
        origin.insert(model.lowercase())
    }

    override suspend fun delete(model: BookingStored) {
        origin.delete(model.lowercase())
    }

    override suspend fun update(model: BookingStored) {
        origin.update(model.lowercase())
    }

    // ---

    private fun BookingStored.lowercase() = copy(
        id = id.lowercase(),
        movieId = movieId.lowercase(),
        eventId = eventId.lowercase(),
        cinemaId = cinemaId.lowercase()
    )

}