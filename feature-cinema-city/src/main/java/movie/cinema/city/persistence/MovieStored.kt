package movie.cinema.city.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
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
        primaryKeys = ["movie", "name"],
        foreignKeys = [ForeignKey(MovieStored::class, ["id"], ["movie"], ForeignKey.CASCADE)]
    )
    data class Cast(
        @ColumnInfo("movie") val movie: String,
        @ColumnInfo("name") val name: String
    )

    @Entity(
        tableName = "movies-director",
        primaryKeys = ["movie", "name"],
        foreignKeys = [ForeignKey(MovieStored::class, ["id"], ["movie"], ForeignKey.CASCADE)]
    )
    data class Director(
        @ColumnInfo("movie") val movie: String,
        @ColumnInfo("name") val name: String
    )

    @Entity(
        tableName = "movies-genre",
        primaryKeys = ["movie", "genre"],
        foreignKeys = [ForeignKey(MovieStored::class, ["id"], ["movie"], ForeignKey.CASCADE)]
    )
    data class Genre(
        @ColumnInfo("movie") val movie: String,
        @ColumnInfo("genre") val genre: String
    )

    @Entity(
        tableName = "movies-video",
        primaryKeys = ["movie", "url"],
        foreignKeys = [ForeignKey(MovieStored::class, ["id"], ["movie"], ForeignKey.CASCADE)]
    )
    data class Video(
        @ColumnInfo("movie") val movie: String,
        @ColumnInfo("url") val url: URL
    )

    @Entity(
        tableName = "movies-image",
        primaryKeys = ["movie", "url", "width", "height"],
        foreignKeys = [ForeignKey(MovieStored::class, ["id"], ["movie"], ForeignKey.CASCADE)]
    )
    data class Image(
        @ColumnInfo("movie") val movie: String,
        @ColumnInfo("width") val width: Int,
        @ColumnInfo("height") val height: Int,
        @ColumnInfo("url") val url: URL
    )

}