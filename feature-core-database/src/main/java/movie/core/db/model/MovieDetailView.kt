package movie.core.db.model

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import java.util.Date
import kotlin.time.Duration.Companion.milliseconds

@DatabaseView(
    value = "select id, name, url, released_at, duration, original_name, country_of_origin, `cast`, directors, description, screening_from, age_restriction_url, rating, link_imdb, link_rt, link_csfd from movies join movie_details on movie_details.movie=movies.id join movie_ratings on movie_ratings.movie=movies.id",
    viewName = "movie_detail_views"
)
data class MovieDetailView(
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("name")
    val name: String,
    @ColumnInfo("url")
    val url: String,
    @ColumnInfo("released_at")
    val releasedAt: Date,
    @ColumnInfo("duration")
    val durationMillis: Long,
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
    @ColumnInfo("rating")
    val rating: Byte,
    @ColumnInfo("link_imdb")
    val linkImdb: String?,
    @ColumnInfo("link_rt")
    val linkRottenTomatoes: String?,
    @ColumnInfo("link_csfd")
    val linkCsfd: String?,
) {

    val duration get() = durationMillis.milliseconds

}