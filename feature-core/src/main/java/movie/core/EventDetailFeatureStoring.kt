package movie.core

import movie.core.adapter.asStored
import movie.core.db.dao.DaoBase.Companion.insertOrElse
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

    override suspend fun get(movie: Movie): MovieDetail {
        return origin.get(movie).also {
            store(it)
        }
    }

    private suspend fun store(model: MovieDetail) {
        movie.insertOrElse((model as Movie).asStored()) {}
        detail.insertOrUpdate(model.asStored())
        for (item in model.media)
            media.insertOrUpdate(item.asStored(model))
    }

}