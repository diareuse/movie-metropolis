package movie.core

import movie.core.adapter.asStored
import movie.core.db.dao.CinemaDao
import movie.core.db.dao.MovieDao
import movie.core.db.dao.MovieDetailDao
import movie.core.db.dao.MovieMediaDao
import movie.core.db.dao.MoviePreviewDao
import movie.core.db.dao.MovieReferenceDao
import movie.core.db.dao.ShowingDao
import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.model.Movie
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
        previewDao.deleteAll(upcoming = false)
        for (preview in it) {
            movieDao.insertOrUpdate((preview as Movie).asStored())
            previewDao.insertOrUpdate(preview.asStored(upcoming = false))
            for (media in preview.media)
                mediaDao.insertOrUpdate(media.asStored(preview))
        }
    }

    override suspend fun getUpcoming() = origin.getUpcoming().onSuccess {
        previewDao.deleteAll(upcoming = false)
        for (preview in it) {
            movieDao.insertOrUpdate((preview as Movie).asStored())
            previewDao.insertOrUpdate(preview.asStored(upcoming = true))
            for (media in preview.media)
                mediaDao.insertOrUpdate(media.asStored(preview))
        }
    }

}