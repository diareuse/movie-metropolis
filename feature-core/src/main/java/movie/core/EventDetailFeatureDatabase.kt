package movie.core

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import movie.core.adapter.MovieDetailFromDatabase
import movie.core.db.dao.MovieDetailDao
import movie.core.db.dao.MovieMediaDao
import movie.core.model.Movie
import movie.core.model.MovieDetail

class EventDetailFeatureDatabase(
    private val detail: MovieDetailDao,
    private val media: MovieMediaDao
) : EventDetailFeature {

    override suspend fun get(movie: Movie): Result<MovieDetail> = kotlin.runCatching {
        coroutineScope {
            val detail = async { detail.select(movie.id) }
            val media = async { media.select(movie.id) }
            MovieDetailFromDatabase(
                detail.await(),
                media.await()
            )
        }
    }

}