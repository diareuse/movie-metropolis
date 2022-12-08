package movie.core.db.model

import androidx.room.ColumnInfo

data class MovieMediaView(
    @ColumnInfo("width")
    val width: Int?,
    @ColumnInfo("height")
    val height: Int?,
    @ColumnInfo("url")
    val url: String,
    @ColumnInfo("type")
    val type: MovieMediaStored.Type
)