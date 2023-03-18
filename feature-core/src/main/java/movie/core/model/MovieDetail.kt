package movie.core.model

import java.util.Date

interface MovieDetail : Movie {
    val originalName: String
    val countryOfOrigin: String?
    val cast: Iterable<String>
    val directors: Iterable<String>
    val description: String
    val screeningFrom: Date
    val ageRestrictionUrl: String
    val media: Iterable<Media>
    val rating: Byte
    val linkImdb: String?
    val linkRottenTomatoes: String?
    val linkCsfd: String?
}