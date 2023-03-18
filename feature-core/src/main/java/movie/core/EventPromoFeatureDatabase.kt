package movie.core

import movie.core.adapter.MoviePromoPosterFromDatabase
import movie.core.db.dao.MoviePromoDao
import movie.core.model.Movie
import movie.core.model.MoviePromoPoster

class EventPromoFeatureDatabase(
    private val dao: MoviePromoDao
) : EventPromoFeature {

    override suspend fun get(movie: Movie): Result<MoviePromoPoster> = dao
        .runCatching { select(movie.id) }
        .mapCatching(::requireNotNull)
        .map(::MoviePromoPosterFromDatabase)

}