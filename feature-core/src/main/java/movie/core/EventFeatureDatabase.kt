package movie.core

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import movie.core.adapter.CinemaFromDatabase
import movie.core.adapter.MovieDetailFromDatabase
import movie.core.db.dao.CinemaDao
import movie.core.db.dao.MovieDetailDao
import movie.core.db.dao.MovieMediaDao
import movie.core.db.dao.MoviePreviewDao
import movie.core.db.dao.MovieReferenceDao
import movie.core.db.dao.ShowingDao
import movie.core.db.model.MovieReferenceView
import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.model.Movie
import movie.core.model.MovieReference
import java.util.Calendar
import java.util.Date

class EventFeatureDatabase(
    private val showingDao: ShowingDao,
    private val cinemaDao: CinemaDao,
    private val movieDao: MovieDetailDao,
    private val mediaDao: MovieMediaDao,
    private val referenceDao: MovieReferenceDao,
    private val previewDao: MoviePreviewDao
) : EventFeature {

    override suspend fun getShowings(
        cinema: Cinema,
        at: Date
    ) = showingDao.selectByCinema(
        rangeStart = at.dayStart.time.coerceAtLeast(Date().time),
        rangeEnd = at.dayEnd.time,
        cinema = cinema.id
    )
        .groupBy { it.movie }
        .mapKeys { (key, _) ->
            referenceDao.select(key)
                .let<MovieReferenceView, MovieReference>(::MovieReferenceFromDatabase)
        }
        .mapValues { (_, value) ->
            value.map { ShowingFromDatabase(it, cinema) }
        }
        .let(Result.Companion::success)

    override suspend fun getCinemas(location: Location?) = cinemaDao.selectAll()
        .map(::CinemaFromDatabase)
        .let(Result.Companion::success)

    override suspend fun getDetail(movie: Movie) = coroutineScope {
        val detail = async { movieDao.select(movie.id) }
        val media = async { mediaDao.select(movie.id) }
        MovieDetailFromDatabase(detail.await(), media.await()).let(Result.Companion::success)
    }

    override suspend fun getCurrent() = previewDao.selectCurrent()
        .map { MoviePreviewFromDatabase(it, mediaDao.select(it.id)) }
        .let(Result.Companion::success)

    override suspend fun getUpcoming() = previewDao.selectUpcoming()
        .map { MoviePreviewFromDatabase(it, mediaDao.select(it.id)) }
        .let(Result.Companion::success)

}

val Date.dayStart
    get() = Calendar.getInstance().let {
        it.time = this
        it.set(Calendar.HOUR_OF_DAY, 0)
        it.set(Calendar.MINUTE, 0)
        it.set(Calendar.SECOND, 0)
        it.set(Calendar.MILLISECOND, 0)
        it.time
    }

val Date.dayEnd
    get() = Calendar.getInstance().let {
        it.time = dayStart
        it.add(Calendar.DAY_OF_YEAR, 1)
        it.add(Calendar.MILLISECOND, -1)
        it.time
    }