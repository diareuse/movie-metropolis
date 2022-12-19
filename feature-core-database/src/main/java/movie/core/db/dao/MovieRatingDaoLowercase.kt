package movie.core.db.dao

import movie.core.db.model.MovieRatingStored

class MovieRatingDaoLowercase(
    private val origin: MovieRatingDao
) : MovieRatingDao {

    override suspend fun insert(model: MovieRatingStored) {
        origin.insert(model.lowercase())
    }

    override suspend fun delete(model: MovieRatingStored) {
        origin.delete(model.lowercase())
    }

    override suspend fun update(model: MovieRatingStored) {
        origin.update(model.lowercase())
    }

    // ---

    private fun MovieRatingStored.lowercase() = copy(movie = movie.lowercase())

}