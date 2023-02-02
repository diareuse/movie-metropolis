package movie.core

import movie.core.adapter.MovieDetailWithRating
import movie.core.db.dao.MovieRatingDao
import movie.core.db.model.MovieRatingStored
import movie.core.model.Movie
import movie.core.model.MovieDetail
import movie.rating.MovieDescriptor
import movie.rating.RatingProvider
import java.util.Calendar

class EventDetailFeatureNetworkRating(
    private val origin: EventDetailFeature,
    private val ratings: MovieRatingDao,
    private val rating: RatingProvider.Composed
) : EventDetailFeature {

    override suspend fun get(movie: Movie, result: ResultCallback<MovieDetail>) {
        origin.get(movie, result.thenMap { detail ->
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
                descriptorLocal,
                descriptorLeanback,
                descriptorLeanbackLocal
            )
            ratings.insertOrUpdate(rating)
            MovieDetailWithRating(detail, rating)
        })
    }

    private suspend fun getRatingStored(
        id: String,
        vararg descriptors: MovieDescriptor
    ): MovieRatingStored {
        val composed = rating.get(descriptors = descriptors)
        val rating = composed.max?.value ?: 0
        return MovieRatingStored(
            movie = id,
            rating = rating,
            linkImdb = composed.imdb?.url,
            linkCsfd = composed.csfd?.url,
            linkRottenTomatoes = composed.rottenTomatoes?.url
        )
    }

}