package movie.cinema.city.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import movie.cinema.city.Movie
import java.net.URL
import java.util.Date

@Entity(tableName = "movies")
internal data class MovieStored(
    @PrimaryKey
    @ColumnInfo("id") val id: String,
    @ColumnInfo("nameLocalized") val nameLocalized: String,
    @ColumnInfo("nameOriginal") val nameOriginal: String,
    @ColumnInfo("length") val length: Long?,
    @ColumnInfo("releasedAt") val releasedAt: Date,
    @ColumnInfo("link") val link: URL,
    @ColumnInfo("originCountry") val originCountry: String?,
    @ColumnInfo("synopsis") val synopsis: String,
    @ColumnInfo("screeningFrom") val screeningFrom: Date,
    @ColumnInfo("ageRestriction") val ageRestriction: URL
) {

    constructor(movie: Movie) : this(
        movie.id,
        movie.name.localized,
        movie.name.original,
        movie.length?.inWholeMilliseconds,
        movie.releasedAt,
        movie.link,
        movie.originCountry,
        movie.synopsis,
        movie.screeningFrom,
        movie.ageRestriction
    )

    @Entity(
        tableName = "movies-cast",
        primaryKeys = ["movie", "name"]
    )
    data class Cast(
        val movie: String,
        val name: String
    )

    @Entity(
        tableName = "movies-director",
        primaryKeys = ["movie", "name"]
    )
    data class Director(
        val movie: String,
        val name: String
    )

    @Entity(
        tableName = "movies-genre",
        primaryKeys = ["movie", "genre"]
    )
    data class Genre(
        val movie: String,
        val genre: String
    )

    @Entity(
        tableName = "movies-video",
        primaryKeys = ["movie", "url"]
    )
    data class Video(
        val movie: String,
        val url: URL
    )

    @Entity(
        tableName = "movies-image",
        primaryKeys = ["movie", "url", "width", "height"]
    )
    data class Image(
        val movie: String,
        val width: Int,
        val height: Int,
        val url: URL
    )

}