package movie.core.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "movie_details",
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
data class MovieDetailStored(
    @PrimaryKey
    @ColumnInfo("movie")
    val movie: String,
    @ColumnInfo("original_name")
    val originalName: String,
    @ColumnInfo("country_of_origin")
    val countryOfOrigin: String?,
    @ColumnInfo("cast")
    val cast: Iterable<String>,
    @ColumnInfo("directors")
    val directors: Iterable<String>,
    @ColumnInfo("description")
    val description: String,
    @ColumnInfo("screening_from")
    val screeningFrom: Date,
    @ColumnInfo("age_restriction_url")
    val ageRestrictionUrl: String,
    @ColumnInfo("genres")
    val genres: Iterable<String>
)