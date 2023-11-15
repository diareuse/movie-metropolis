package movie.core.db.model

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import java.util.Date
import kotlin.time.Duration.Companion.milliseconds

@DatabaseView(
    value = "select m.id, m.name, m.url, m.released_at, m.duration, md.original_name, md.country_of_origin, md.`cast`, md.directors, mp.description, md.screening_from, md.age_restriction_url, md.genres from movies as m, movie_details as md, movie_previews as mp where md.movie=m.id and mp.movie=md.movie",
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
    @ColumnInfo("genres")
    val genres: Iterable<String>
) {

    val duration get() = durationMillis.milliseconds

}