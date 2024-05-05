package movie.cinema.city

import java.net.URL
import java.util.Date
import kotlin.time.Duration

interface Movie {
    val id: String
    val name: Label

    interface Label {
        val localized: String
        val original: String
    }

    val length: Duration?
    val link: URL
    val releasedAt: Date
    val originCountry: String?
    val cast: List<String>
    val directors: List<String>
    val synopsis: String
    val screeningFrom: Date
    val genres: List<String>
    val ageRestriction: URL
    val videos: List<URL>
    val images: List<Image>

    interface Image {
        val width: Int
        val height: Int
        val url: URL
    }
}