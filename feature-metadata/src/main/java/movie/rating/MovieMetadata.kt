package movie.rating

import java.util.Date

data class MovieMetadata(
    val id: Long,
    val rating: Byte,
    val posterImageUrl: String,
    val overlayImageUrl: String,
    val url: String,
    val releaseDate: Date?,
    val description: String,
    val trailerUrl: String?
)