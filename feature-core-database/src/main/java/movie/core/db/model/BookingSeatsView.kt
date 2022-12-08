package movie.core.db.model

import androidx.room.ColumnInfo

data class BookingSeatsView(
    @ColumnInfo("row")
    val row: String,
    @ColumnInfo("seat")
    val seat: String
)