package movie.core

import movie.core.db.dao.MovieDao
import movie.core.db.dao.MovieDetailDao
import movie.core.db.dao.MovieMediaDao
import movie.core.model.Movie
import movie.core.model.MovieDetail

class EventDetailFeatureStoring(
    private val origin: EventDetailFeature,
    private val movie: MovieDao,
    private val detail: MovieDetailDao,
    private val media: MovieMediaDao
) : EventDetailFeature {

    override suspend fun get(movie: Movie, result: ResultCallback<MovieDetail>) {
        origin.get(movie) {
            result(it)
            it.onSuccess { detail ->
                store(detail)
            }
        }
    }

    private suspend fun store(model: MovieDetail) {
        movie.insertOrUpdate((model as Movie).asStored())
        detail.insertOrUpdate(model.asStored())
        for (item in model.media)
            media.insertOrUpdate(item.asStored(model))
    }

}