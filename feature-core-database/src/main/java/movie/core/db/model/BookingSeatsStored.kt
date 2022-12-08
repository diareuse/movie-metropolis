package movie.core.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "booking_seats",
    primaryKeys = ["booking", "row", "seat"],
    foreignKeys = [
        ForeignKey(
            entity = BookingStored::class,
            parentColumns = ["id"],
            childColumns = ["booking"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("booking")
    ]
)
data class BookingSeatsStored(
    @ColumnInfo("booking")
    val booking: String,
    @ColumnInfo("row")
    val row: String,
    @ColumnInfo("seat")
    val seat: String
)