package movie.core.db.dao

import androidx.room.Dao
import androidx.room.Query
import movie.core.db.model.ShowingStored

@Dao
interface ShowingDao : DaoBase<ShowingStored> {

    @Query("select * from showings where starts_at >= :rangeStart and starts_at <= :rangeEnd and cinema = :cinema order by starts_at asc")
    suspend fun selectByCinema(
        rangeStart: Long,
        rangeEnd: Long,
        cinema: String
    ): List<ShowingStored>

}