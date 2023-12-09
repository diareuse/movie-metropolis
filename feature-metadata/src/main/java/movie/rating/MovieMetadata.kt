package movie.rating

data class MovieMetadata(
    val id: Long,
    val rating: Byte,
    val posterImageUrl: String,
    val overlayImageUrl: String,
    val url: String
)