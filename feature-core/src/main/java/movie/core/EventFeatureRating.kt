package movie.core

import movie.core.db.dao.MovieRatingDao
import movie.core.db.model.MovieRatingStored
import movie.core.model.Movie
import movie.rating.LinkProvider
import movie.rating.MovieDescriptor
import movie.rating.RatingProvider
import movie.rating.getLinkOrNull
import java.util.Calendar

class EventFeatureRating(
    private val origin: EventFeature,
    private val dao: MovieRatingDao,
    private val rating: RatingProvider,
    private val tomatoes: LinkProvider,
    private val imdb: LinkProvider,
    private val csfd: LinkProvider,
) : EventFeature by origin {

    override suspend fun getDetail(movie: Movie) = origin.getDetail(movie).onSuccess {
        val year = Calendar.getInstance().run {
            time = it.releasedAt
            get(Calendar.YEAR)
        }
        val descriptor = MovieDescriptor(it.originalName, year)
        val rating = MovieRatingStored(
            movie = it.id,
            rating = rating.getRating(descriptor),
            linkImdb = imdb.getLinkOrNull(descriptor),
            linkCsfd = csfd.getLinkOrNull(descriptor),
            linkRottenTomatoes = tomatoes.getLinkOrNull(descriptor)
        )
        dao.insertOrUpdate(rating)
    }

}