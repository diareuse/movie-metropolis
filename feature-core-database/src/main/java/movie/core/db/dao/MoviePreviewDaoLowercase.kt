package movie.core.db.dao

import movie.core.db.model.MoviePreviewStored

class MoviePreviewDaoLowercase(
    private val origin: MoviePreviewDao
) : MoviePreviewDao by origin {

    override suspend fun insert(model: MoviePreviewStored) {
        return origin.insert(model.lowercase())
    }

    override suspend fun delete(model: MoviePreviewStored) {
        return origin.delete(model.lowercase())
    }

    override suspend fun update(model: MoviePreviewStored) {
        return origin.update(model.lowercase())
    }

    // ---

    private fun MoviePreviewStored.lowercase() = copy(movie = movie.lowercase())

}