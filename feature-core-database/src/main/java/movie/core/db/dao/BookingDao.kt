package movie.core.db.dao

import androidx.room.Dao
import androidx.room.Query
import movie.core.db.model.BookingStored

@Dao
interface BookingDao : DaoBase<BookingStored> {

    @Query("select * from bookings order by starts_at desc")
    suspend fun selectAll(): List<BookingStored>

    @Query("select distinct id from bookings")
    suspend fun selectIds(): List<String>

}