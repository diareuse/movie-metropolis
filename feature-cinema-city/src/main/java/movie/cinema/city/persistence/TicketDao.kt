package movie.cinema.city.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
internal interface TicketDao {
    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun insertTickets(item: List<TicketStored>)

    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun insertReservations(items: List<TicketStored.Reservation>)

    @Query("select * from tickets order by startsAt desc")
    suspend fun selectTickets(): List<TicketStored>

    @Query("select * from `tickets-reservation` as t where t.ticket=:ticket")
    suspend fun selectReservations(ticket: String): List<TicketStored.Reservation>
}