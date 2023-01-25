package movie.core.adapter

import movie.core.model.Media
import movie.core.model.MovieDetail
import java.util.Date
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class MovieDetailFromId(
    override val id: String
) : MovieDetail {

    override val name: String
        get() = ""
    override val url: String
        get() = ""
    override val releasedAt: Date
        get() = Date()
    override val duration: Duration
        get() = 0.seconds
    override val originalName: String
        get() = ""
    override val countryOfOrigin: String?
        get() = null
    override val cast: Iterable<String>
        get() = emptyList()
    override val directors: Iterable<String>
        get() = emptyList()
    override val description: String
        get() = ""
    override val screeningFrom: Date
        get() = Date()
    override val ageRestrictionUrl: String
        get() = ""
    override val media: Iterable<Media>
        get() = emptyList()
    override val rating: Byte
        get() = 0
    override val linkImdb: String?
        get() = null
    override val linkRottenTomatoes: String?
        get() = null
    override val linkCsfd: String?
        get() = null
    override val spotColor: Int
        get() = 0xff000000.toInt()

}