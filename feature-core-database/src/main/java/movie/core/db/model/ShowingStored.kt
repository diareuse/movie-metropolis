package movie.core.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "showings",
    foreignKeys = [
        ForeignKey(
            entity = CinemaStored::class,
            parentColumns = ["id"],
            childColumns = ["cinema"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("cinema"),
        Index("movie"),
        Index("starts_at", orders = [Index.Order.ASC])
    ]
)
data class ShowingStored(
    @PrimaryKey
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("cinema")
    val cinema: String,
    @ColumnInfo("starts_at")
    val startsAt: Date,
    @ColumnInfo("booking_url")
    val bookingUrl: String,
    @ColumnInfo("is_enabled")
    val isEnabled: Boolean,
    @ColumnInfo("auditorium")
    val auditorium: String,
    @ColumnInfo("language")
    val language: String,
    @ColumnInfo("type")
    val type: String,
    @ColumnInfo("movie")
    val movie: String
)