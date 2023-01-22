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

class EventDetailFeatureNetworkRating(
    private val origin: EventDetailFeature,
    private val ratings: MovieRatingDao,
    private val rating: RatingProvider,
    private val tomatoes: LinkProvider,
    private val imdb: LinkProvider,
    private val csfd: LinkProvider,
) : EventDetailFeature {

    override suspend fun get(movie: Movie, result: ResultCallback<MovieDetail>) {
        origin.get(movie) {
            result(it)
            val output = it.map { detail ->
                val year = Calendar.getInstance().run {
                    time = detail.releasedAt
                    get(Calendar.YEAR)
                }
                val descriptor = MovieDescriptor(detail.originalName, year)
                val descriptorLocal = MovieDescriptor(detail.name, year)
                val descriptorLeanback = MovieDescriptor(detail.originalName, year - 1)
                val descriptorLeanbackLocal = MovieDescriptor(detail.name, year - 1)
                val rating = getRatingStored(
                    detail.id,
                    descriptor,
                    descriptorLeanback,
                    descriptorLocal,
                    descriptorLeanbackLocal
                )
                ratings.insertOrUpdate(rating)
                MovieDetailWithRating(detail, rating)
            }
            result(output)
        }
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