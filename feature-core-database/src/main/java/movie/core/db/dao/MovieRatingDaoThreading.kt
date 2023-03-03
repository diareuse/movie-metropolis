package movie.core.db.dao

import movie.core.db.io
import movie.core.db.model.MovieRatingStored

class MovieRatingDaoThreading(
    private val origin: MovieRatingDao
) : MovieRatingDao {

    override suspend fun insert(model: MovieRatingStored) = origin.io { insert(model) }

    override suspend fun delete(model: MovieRatingStored) = origin.io { delete(model) }

    override suspend fun update(model: MovieRatingStored) = origin.io { update(model) }

    override suspend fun select(id: String) = origin.io { select(id) }

    override suspend fun isRecent(id: String) = origin.io { isRecent(id) }

}