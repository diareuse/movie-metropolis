package movie.core.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import movie.core.db.converters.ShowingTypeConverter
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
@TypeConverters(ShowingTypeConverter::class)
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
    @ColumnInfo("subtitles")
    val subtitles: String?,
    @ColumnInfo("type")
    val types: List<String>,
    @ColumnInfo("movie")
    val movie: String
)