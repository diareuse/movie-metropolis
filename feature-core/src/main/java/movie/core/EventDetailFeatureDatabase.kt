package movie.core

import movie.core.adapter.MovieDetailFromDatabase
import movie.core.db.dao.MovieDetailDao
import movie.core.db.dao.MovieMediaDao
import movie.core.model.Movie
import movie.core.model.MovieDetail

class EventDetailFeatureDatabase(
    private val detail: MovieDetailDao,
    private val media: MovieMediaDao
) : EventDetailFeature {

    override suspend fun get(movie: Movie, result: ResultCallback<MovieDetail>) {
        val output = MovieDetailFromDatabase(
            detail.select(movie.id),
            media.select(movie.id)
        )
        result(Result.success(output))
    }

}