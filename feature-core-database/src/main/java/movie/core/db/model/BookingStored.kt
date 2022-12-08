package movie.core.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "bookings",
    foreignKeys = [
        ForeignKey(
            entity = CinemaStored::class,
            parentColumns = ["id"],
            childColumns = ["cinema"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("cinema")
    ]
)
data class BookingStored(
    @PrimaryKey
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("name")
    val name: String,
    @ColumnInfo("starts_at")
    val startsAt: Date,
    @ColumnInfo("paid_at")
    val paidAt: Date,
    @ColumnInfo("movie")
    val movieId: String,
    @ColumnInfo("event")
    val eventId: String,
    @ColumnInfo("cinema")
    val cinemaId: String,
    @ColumnInfo("hall")
    val hall: String?
)