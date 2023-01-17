package movie.core.db.model

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import java.util.Date
import kotlin.time.Duration.Companion.milliseconds

@DatabaseView(
    value = "select movies.id, movies.name, movies.url, movies.released_at, movies.duration, movie_details.original_name, movie_details.country_of_origin, movie_details.`cast`, movie_details.directors, movie_details.description, movie_details.screening_from, movie_details.age_restriction_url, movie_ratings.rating, movie_ratings.link_imdb, movie_ratings.link_rt, movie_ratings.link_csfd from movies, movie_details, movie_ratings where movie_details.movie=movies.id and movie_ratings.movie=movies.id",
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