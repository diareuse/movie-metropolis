package movie.core.db.dao

import movie.core.db.io
import movie.core.db.model.MoviePromoStored

class MoviePromoDaoThreading(
    private val origin: MoviePromoDao
) : MoviePromoDao {

    override suspend fun insert(model: MoviePromoStored) = origin.io { insert(model) }

    override suspend fun delete(model: MoviePromoStored) = origin.io { delete(model) }

    override suspend fun update(model: MoviePromoStored) = origin.io { update(model) }

    override suspend fun select(id: String): String? = origin.io { select(id) }

}