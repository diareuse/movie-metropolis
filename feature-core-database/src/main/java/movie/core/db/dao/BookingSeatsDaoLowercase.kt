package movie.core.db.dao

import movie.core.db.model.BookingSeatsStored
import movie.core.db.model.BookingSeatsView

class BookingSeatsDaoLowercase(
    private val origin: BookingSeatsDao
) : BookingSeatsDao by origin {

    override suspend fun select(id: String): List<BookingSeatsView> {
        return origin.select(id.lowercase())
    }

    override suspend fun deleteFor(id: String) {
        return origin.deleteFor(id.lowercase())
    }

    override suspend fun insert(model: BookingSeatsStored) {
        return origin.insert(model.lowercase())
    }

    override suspend fun delete(model: BookingSeatsStored) {
        return origin.delete(model.lowercase())
    }

    override suspend fun update(model: BookingSeatsStored) {
        return origin.update(model.lowercase())
    }

    // ---

    private fun BookingSeatsStored.lowercase() = copy(
        booking = booking.lowercase()
    )

}