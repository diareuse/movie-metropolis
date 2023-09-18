package movie.rating

data class MovieMetadata(
    val rating: Byte,
    val posterImageUrl: String,
    val overlayImageUrl: String
)