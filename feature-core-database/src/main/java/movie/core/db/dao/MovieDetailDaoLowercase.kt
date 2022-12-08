package movie.core.db.dao

import movie.core.db.model.MovieDetailStored
import movie.core.db.model.MovieDetailView

class MovieDetailDaoLowercase(
    private val origin: MovieDetailDao
) : MovieDetailDao by origin {

    override suspend fun insert(model: MovieDetailStored) {
        return origin.insert(model.lowercase())
    }

    override suspend fun delete(model: MovieDetailStored) {
        return origin.delete(model.lowercase())
    }

    override suspend fun update(model: MovieDetailStored) {
        return origin.update(model.lowercase())
    }

    override suspend fun select(id: String): MovieDetailView {
        return origin.select(id.lowercase())
    }

    // ---

    private fun MovieDetailStored.lowercase() = copy(movie = movie.lowercase())

}