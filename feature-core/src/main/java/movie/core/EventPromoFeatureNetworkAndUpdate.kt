package movie.core

import movie.core.adapter.MoviePromoPosterFromDatabase
import movie.core.db.dao.MoviePromoDao
import movie.core.db.model.MoviePromoStored
import movie.core.model.Movie
import movie.core.model.MoviePromoPoster
import movie.core.nwk.CinemaService

class EventPromoFeatureNetworkAndUpdate(
    private val service: CinemaService,
    private val dao: MoviePromoDao
) : EventPromoFeature {

    override suspend fun get(movie: Movie, callback: ResultCallback<MoviePromoPoster>) {
        val items = service.getPromoCards().getOrThrow().results
            .map { MoviePromoStored(it.id, it.imageId) }
        val entry = items.first { it.movie == movie.id }
        callback(Result.success(MoviePromoPosterFromDatabase(entry.url)))
        for (item in items)
            dao.insertOrUpdate(item)
    }

}