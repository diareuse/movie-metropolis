package movie.core.mock

import movie.core.db.model.MovieFavoriteStored
import movie.core.db.model.MovieStored
import java.util.Date

fun MovieFavoriteStored(notified: Boolean = false) = mockFinal<MovieFavoriteStored> {
    override { Date(0) }
    set(MovieFavoriteStored::movie, "id")
    set(MovieFavoriteStored::notified, notified)
}

fun MovieStored() = mockFinal<MovieStored> {
    override { Date(0) }
}