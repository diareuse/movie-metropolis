package movie.core.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "movie_previews",
    foreignKeys = [
        ForeignKey(
            entity = MovieStored::class,
            parentColumns = ["id"],
            childColumns = ["movie"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("movie")
    ]
)
data class MoviePreviewStored(
    @PrimaryKey
    @ColumnInfo("movie")
    val movie: String,
    @ColumnInfo("screening_from")
    val screeningFrom: Date,
    @ColumnInfo("description")
    val description: String,
    @ColumnInfo("directors")
    val directors: Iterable<String>,
    @ColumnInfo("cast")
    val cast: Iterable<String>,
    @ColumnInfo("country_of_origin")
    val countryOfOrigin: String,
    @ColumnInfo("upcoming")
    val isUpcoming: Boolean,
    @ColumnInfo("genres")
    val genres: Iterable<String>
)