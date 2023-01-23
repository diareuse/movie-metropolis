package movie.core.db.dao

import movie.core.db.model.MovieRatingStored

class MovieRatingDaoLowercase(
    private val origin: MovieRatingDao
) : MovieRatingDao by origin {

    override suspend fun insert(model: MovieRatingStored) {
        origin.insert(model.lowercase())
    }

    override suspend fun delete(model: MovieRatingStored) {
        origin.delete(model.lowercase())
    }

    override suspend fun update(model: MovieRatingStored) {
        origin.update(model.lowercase())
    }

    override suspend fun select(id: String): Byte? {
        return origin.select(id.lowercase())
    }

    // ---

    private fun MovieRatingStored.lowercase() = copy(movie = movie.lowercase())

}