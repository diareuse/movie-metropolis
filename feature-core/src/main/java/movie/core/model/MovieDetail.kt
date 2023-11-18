package movie.core.model

interface MovieDetail : Movie {
    val originalName: String
    val countryOfOrigin: String?
    val cast: Iterable<String>
    val directors: Iterable<String>
    val description: String
    val ageRestrictionUrl: String
    val media: Iterable<Media>
    val genres: Iterable<String>
}