package movie.core

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import movie.core.adapter.MovieDetailWithRating
import movie.core.db.dao.MovieRatingDao
import movie.core.db.model.MovieRatingStored
import movie.core.model.Movie
import movie.core.model.MovieDetail
import movie.rating.LinkProvider
import movie.rating.MovieDescriptor
import movie.rating.RatingProvider
import movie.rating.getLinkOrNull
import movie.rating.getRating
import java.util.Calendar

class EventFeatureRating(
    private val origin: EventFeature,
    private val dao: MovieRatingDao,
    private val rating: RatingProvider,
    private val tomatoes: LinkProvider,
    private val imdb: LinkProvider,
    private val csfd: LinkProvider,
) : EventFeature by origin {

    override suspend fun getDetail(
        movie: Movie
    ): Result<MovieDetail> = origin.getDetail(movie).map {
        val year = Calendar.getInstance().run {
            time = it.releasedAt
            get(Calendar.YEAR)
        }
        val descriptor = MovieDescriptor(it.originalName, year)
        val descriptorLocal = MovieDescriptor(it.name, year)
        val rating = getRatingStored(it.id, descriptor, descriptorLocal)
        dao.insertOrUpdate(rating)
        MovieDetailWithRating(it, rating)
    }

    private suspend fun getRatingStored(
        id: String,
        vararg descriptors: MovieDescriptor
    ): MovieRatingStored = coroutineScope {
        val rating = async { rating.getRating(descriptors = descriptors) }
        val imdb = async { imdb.getLinkOrNull(descriptors = descriptors) }
        val csfd = async { csfd.getLinkOrNull(descriptors = descriptors) }
        val tomatoes = async { tomatoes.getLinkOrNull(descriptors = descriptors) }
        MovieRatingStored(
            movie = id,
            rating = rating.await(),
            linkImdb = imdb.await(),
            linkCsfd = csfd.await(),
            linkRottenTomatoes = tomatoes.await()
        )
    }

}