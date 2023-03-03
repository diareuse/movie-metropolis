package movie.core.db.dao

import movie.core.db.io
import movie.core.db.model.MovieReferenceStored
import movie.core.db.model.MovieReferenceView

class MovieReferenceDaoThreading(
    private val origin: MovieReferenceDao
) : MovieReferenceDao {

    override suspend fun insert(model: MovieReferenceStored) = origin.io { insert(model) }

    override suspend fun delete(model: MovieReferenceStored) = origin.io { delete(model) }

    override suspend fun update(model: MovieReferenceStored) = origin.io { update(model) }

    override suspend fun select(id: String): MovieReferenceView = origin.io { select(id) }

}