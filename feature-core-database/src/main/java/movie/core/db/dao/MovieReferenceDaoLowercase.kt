package movie.core.db.dao

import movie.core.db.model.MovieReferenceStored
import movie.core.db.model.MovieReferenceView

class MovieReferenceDaoLowercase(
    private val origin: MovieReferenceDao
) : MovieReferenceDao by origin {

    override suspend fun insert(model: MovieReferenceStored) {
        return origin.insert(model.lowercase())
    }

    override suspend fun delete(model: MovieReferenceStored) {
        return origin.delete(model.lowercase())
    }

    override suspend fun update(model: MovieReferenceStored) {
        return origin.update(model.lowercase())
    }

    override suspend fun select(id: String): MovieReferenceView {
        return origin.select(id.lowercase())
    }

    // ---

    private fun MovieReferenceStored.lowercase() = copy(movie = movie.lowercase())

}