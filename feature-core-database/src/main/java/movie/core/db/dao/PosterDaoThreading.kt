package movie.core.db.dao

import movie.core.db.io

class PosterDaoThreading(
    private val origin: PosterDao
) : PosterDao {

    override suspend fun selectAll() = origin.io { selectAll() }

}