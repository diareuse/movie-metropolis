package movie.core.db.dao

import movie.core.db.model.MovieMediaStored
import movie.core.db.model.MovieMediaView

class MovieMediaDaoLowercase(
    private val origin: MovieMediaDao
) : MovieMediaDao by origin {

    override suspend fun insert(model: MovieMediaStored) {
        return origin.insert(model.lowercase())
    }

    override suspend fun delete(model: MovieMediaStored) {
        return origin.delete(model.lowercase())
    }

    override suspend fun update(model: MovieMediaStored) {
        return origin.update(model.lowercase())
    }

    override suspend fun select(id: String): List<MovieMediaView> {
        return origin.select(id.lowercase())
    }

    // ---

    private fun MovieMediaStored.lowercase() = copy(movie = movie.lowercase())

}