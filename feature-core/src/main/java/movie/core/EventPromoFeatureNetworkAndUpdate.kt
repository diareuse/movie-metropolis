package movie.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import movie.core.adapter.MoviePromoPosterFromDatabase
import movie.core.db.dao.MoviePromoDao
import movie.core.db.model.MoviePromoStored
import movie.core.model.Movie
import movie.core.model.MoviePromoPoster
import movie.core.nwk.CinemaService

class EventPromoFeatureNetworkAndUpdate(
    private val service: CinemaService,
    private val dao: MoviePromoDao,
    private val scope: CoroutineScope
) : EventPromoFeature {

    override suspend fun get(movie: Movie): Result<MoviePromoPoster> {
        return service.getPromoCards()
            .map { it.results.map { MoviePromoStored(it.id, it.imageId) } }
            .onSuccess {
                scope.launch {
                    for (item in it)
                        dao.insertOrUpdate(item)
                }
            }
            .mapCatching { it.first { it.movie == movie.id }.url }
            .map(::MoviePromoPosterFromDatabase)
    }

}