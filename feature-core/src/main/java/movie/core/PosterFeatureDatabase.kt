package movie.core

import movie.core.db.dao.PosterDao

class PosterFeatureDatabase(
    private val dao: PosterDao
) : PosterFeature {

    override suspend fun get(): List<String> {
        return dao.selectAll()
    }

}