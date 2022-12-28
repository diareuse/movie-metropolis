package movie.image.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "colors",
    indices = [Index("image")],
    foreignKeys = [
        ForeignKey(
            entity = ImageStored::class,
            parentColumns = ["url"],
            childColumns = ["image"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["image", "color"]
)
internal data class ColorStored(
    @ColumnInfo("image")
    val image: String,
    @ColumnInfo("color")
    val color: Int
)