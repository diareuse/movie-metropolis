package movie.image.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageStored(
    @PrimaryKey
    @ColumnInfo("url")
    val url: String
)