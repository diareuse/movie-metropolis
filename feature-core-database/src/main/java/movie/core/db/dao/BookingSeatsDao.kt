package movie.core.db.dao

import androidx.room.Dao
import androidx.room.Query
import movie.core.db.model.BookingSeatsStored
import movie.core.db.model.BookingSeatsView

@Dao
interface BookingSeatsDao : DaoBase<BookingSeatsStored> {

    @Query("select `row`,seat from booking_seats where booking=:id")
    suspend fun select(id: String): List<BookingSeatsView>

    @Query("delete from booking_seats where booking=:id")
    suspend fun deleteFor(id: String)

}