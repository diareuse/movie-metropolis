package movie.cinema.city.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import movie.cinema.city.Ticket
import java.util.Date

@Entity(tableName = "tickets")
internal data class TicketStored(
    @PrimaryKey
    @ColumnInfo("id") val id: String,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("startsAt") val startsAt: Date,
    @ColumnInfo("paidAt") val paidAt: Date?,
    @ColumnInfo("movie") val movie: String,
    @ColumnInfo("cinema") val cinema: String,
    @ColumnInfo("venue") val venue: String
) {

    constructor(ticket: Ticket) : this(
        id = ticket.id,
        name = ticket.name,
        startsAt = ticket.startsAt,
        paidAt = ticket.paidAt,
        movie = ticket.movie.id,
        cinema = ticket.cinema.id,
        venue = ticket.venue.name
    )

    @Entity(
        tableName = "tickets-reservation",
        primaryKeys = ["ticket", "row", "seat"],
        foreignKeys = [ForeignKey(TicketStored::class, ["id"], ["ticket"], ForeignKey.CASCADE)]
    )
    data class Reservation(
        @ColumnInfo("ticket") val ticket: String,
        @ColumnInfo("row") val row: String,
        @ColumnInfo("seat") val seat: String
    )

}