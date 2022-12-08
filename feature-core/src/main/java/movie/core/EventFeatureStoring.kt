package movie.core

import movie.core.db.dao.CinemaDao
import movie.core.db.dao.MovieDao
import movie.core.db.dao.MovieDetailDao
import movie.core.db.dao.MovieMediaDao
import movie.core.db.dao.MoviePreviewDao
import movie.core.db.dao.MovieReferenceDao
import movie.core.db.dao.ShowingDao
import movie.core.db.model.CinemaStored
import movie.core.db.model.MovieDetailStored
import movie.core.db.model.MovieMediaStored
import movie.core.db.model.MoviePreviewStored
import movie.core.db.model.MovieReferenceStored
import movie.core.db.model.MovieStored
import movie.core.db.model.ShowingStored
import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.model.Media
import movie.core.model.Movie
import movie.core.model.MovieDetail
import movie.core.model.MoviePreview
import movie.core.model.MovieReference
import movie.core.model.Showing
import java.util.Date

class EventFeatureStoring(
    private val origin: EventFeature,
    private val cinemaDao: CinemaDao,
    private val movieDao: MovieDao,
    private val detailDao: MovieDetailDao,
    private val mediaDao: MovieMediaDao,
    private val showingDao: ShowingDao,
    private val referenceDao: MovieReferenceDao,
    private val previewDao: MoviePreviewDao
) : EventFeature {

    override suspend fun getShowings(
        cinema: Cinema,
        at: Date
    ) = origin.getShowings(cinema, at).onSuccess {
        for ((movie, showings) in it) {
            movieDao.insertOrUpdate((movie as Movie).asStored())
            referenceDao.insertOrUpdate(movie.asStored())
            for (showing in showings)
                showingDao.insertOrUpdate(showing.asStored(movie))
        }
    }

    override suspend fun getCinemas(location: Location?) = origin.getCinemas(location).onSuccess {
        if (location != null) return@onSuccess
        for (cinema in it)
            cinemaDao.insertOrUpdate(cinema.asStored())
    }

    override suspend fun getDetail(movie: Movie) = origin.getDetail(movie).onSuccess {
        movieDao.insertOrUpdate((it as Movie).asStored())
        detailDao.insertOrUpdate(it.asStored())
        for (media in it.media)
            mediaDao.insertOrUpdate(media.asStored(it))
    }

    override suspend fun getCurrent() = origin.getCurrent().onSuccess {
        for (preview in it) {
            movieDao.insertOrUpdate((preview as Movie).asStored())
            previewDao.insertOrUpdate(preview.asStored(upcoming = false))
            for (media in preview.media)
                mediaDao.insertOrUpdate(media.asStored(preview))
        }
    }

    override suspend fun getUpcoming() = origin.getUpcoming().onSuccess {
        for (preview in it) {
            movieDao.insertOrUpdate((preview as Movie).asStored())
            previewDao.insertOrUpdate(preview.asStored(upcoming = true))
            for (media in preview.media)
                mediaDao.insertOrUpdate(media.asStored(preview))
        }
    }

    // ---

    private fun Cinema.asStored() = CinemaStored(
        id = id,
        name = name,
        description = description,
        city = city,
        address = address,
        latitude = location.latitude,
        longitude = location.longitude
    )

    private fun Movie.asStored() = MovieStored(
        id = id,
        name = name,
        url = url,
        releasedAt = releasedAt,
        durationMillis = duration.inWholeMilliseconds
    )

    private fun MovieDetail.asStored() = MovieDetailStored(
        movie = id,
        originalName = originalName,
        countryOfOrigin = countryOfOrigin,
        cast = cast,
        directors = directors,
        description = description,
        screeningFrom = screeningFrom,
        ageRestrictionUrl = ageRestrictionUrl
    )

    private fun Media.asStored(movie: Movie) = when (this) {
        is Media.Image -> asStored(movie)
        is Media.Video -> asStored(movie)
    }

    private fun Media.Image.asStored(movie: Movie) = MovieMediaStored(
        movie = movie.id,
        width = width,
        height = height,
        url = url,
        type = MovieMediaStored.Type.Image
    )

    private fun Media.Video.asStored(movie: Movie) = MovieMediaStored(
        movie = movie.id,
        width = null,
        height = null,
        url = url,
        type = MovieMediaStored.Type.Video
    )

    private fun MovieReference.asStored() = MovieReferenceStored(
        movie = id,
        poster = posterUrl,
        video = videoUrl
    )

    private fun Showing.asStored(movie: Movie) = ShowingStored(
        id = id,
        cinema = cinema.id,
        startsAt = startsAt,
        bookingUrl = bookingUrl,
        isEnabled = isEnabled,
        auditorium = auditorium,
        language = language,
        type = type,
        movie = movie.id
    )

    private fun MoviePreview.asStored(upcoming: Boolean) = MoviePreviewStored(
        movie = id,
        screeningFrom = screeningFrom,
        description = description,
        directors = directors,
        cast = cast,
        countryOfOrigin = countryOfOrigin,
        isUpcoming = upcoming
    )

}