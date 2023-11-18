package movie.core.model

import java.util.Date

interface MovieFavorite {
    val movie: Movie
    val createdAt: Date
    val isNotified: Boolean
}