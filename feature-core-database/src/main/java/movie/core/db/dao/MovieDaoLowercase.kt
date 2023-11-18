package movie.core.db.dao

import movie.core.db.model.MovieStored

class MovieDaoLowercase(
    private val origin: MovieDao
) : MovieDao by origin {

    override suspend fun insert(model: MovieStored) {
        return origin.insert(model.lowercase())
    }

    override suspend fun delete(model: MovieStored) {
        return origin.delete(model.lowercase())
    }

    override suspend fun update(model: MovieStored) {
        return origin.update(model.lowercase())
    }

    override suspend fun select(id: String): MovieStored? {
        return origin.select(id.lowercase())
    }

    // ---

    private fun MovieStored.lowercase() = copy(id = id.lowercase())

}