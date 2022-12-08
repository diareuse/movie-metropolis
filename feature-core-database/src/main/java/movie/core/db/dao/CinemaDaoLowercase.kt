package movie.core.db.dao

import movie.core.db.model.CinemaStored

class CinemaDaoLowercase(
    private val origin: CinemaDao
) : CinemaDao by origin {

    override suspend fun select(id: String): CinemaStored {
        return origin.select(id.lowercase())
    }

    override suspend fun insert(model: CinemaStored) {
        return origin.insert(model.lowercase())
    }

    override suspend fun delete(model: CinemaStored) {
        return origin.delete(model.lowercase())
    }

    override suspend fun update(model: CinemaStored) {
        return origin.update(model.lowercase())
    }

    // ---

    private fun CinemaStored.lowercase() = copy(id = id.lowercase())

}