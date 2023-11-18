package movie.core.adapter

import movie.core.db.model.MovieFavoriteStored
import movie.core.model.Movie
import movie.core.model.MovieFavorite
import java.util.Date

data class MovieFavoriteFromDatabase(
    private val stored: MovieFavoriteStored,
    override val movie: Movie
) : MovieFavorite {
    override val createdAt: Date
        get() = stored.createdAt
    override val isNotified: Boolean
        get() = stored.notified
}