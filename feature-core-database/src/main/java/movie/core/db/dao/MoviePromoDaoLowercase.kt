package movie.core.db.dao

import movie.core.db.model.MoviePromoStored

class MoviePromoDaoLowercase(
    private val origin: MoviePromoDao
) : MoviePromoDao {

    override suspend fun insert(model: MoviePromoStored) {
        origin.insert(model.lowercase())
    }

    override suspend fun delete(model: MoviePromoStored) {
        origin.delete(model.lowercase())
    }

    override suspend fun update(model: MoviePromoStored) {
        origin.update(model.lowercase())
    }

    override suspend fun select(id: String): String? {
        return origin.select(id.lowercase())
    }

    // ---

    private fun MoviePromoStored.lowercase() = copy(
        movie = movie.lowercase()
    )

}