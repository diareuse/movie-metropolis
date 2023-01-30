package movie.core

import movie.core.adapter.MoviePromoPosterFromDatabase
import movie.core.db.dao.MoviePromoDao
import movie.core.model.Movie
import movie.core.model.MoviePromoPoster

class EventPromoFeatureDatabase(
    private val dao: MoviePromoDao
) : EventPromoFeature {

    override suspend fun get(movie: Movie, callback: ResultCallback<MoviePromoPoster>) {
        val stored = dao.select(movie.id).let(::requireNotNull)
        callback(Result.success(stored.let(::MoviePromoPosterFromDatabase)))
    }

}