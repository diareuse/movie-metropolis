package movie.metropolis.app.screen.detail

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import movie.metropolis.app.feature.global.EventFeature
import movie.metropolis.app.feature.global.model.Location
import movie.metropolis.app.feature.global.model.Media
import movie.metropolis.app.feature.global.model.MovieDetail
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.model.adapter.CinemaBookingViewFromFeature
import movie.metropolis.app.model.adapter.ImageViewFromFeature
import movie.metropolis.app.model.adapter.MovieDetailViewFromFeature
import movie.metropolis.app.model.adapter.MovieFromId
import movie.metropolis.app.model.adapter.VideoViewFromFeature
import java.util.Date

class MovieFacadeFromFeature(
    private val id: String,
    private val event: EventFeature
) : MovieFacade {

    private val mutex = Mutex(false)
    private var movie: MovieDetail? = null

    override suspend fun getAvailableFrom(): Result<Date> {
        return Result.success(getDetail().screeningFrom)
    }

    override suspend fun getMovie(): Result<MovieDetailView> {
        return Result.success(MovieDetailViewFromFeature(getDetail()))
    }

    override suspend fun getPoster(): Result<ImageView> {
        val image = getDetail().media
            .asSequence()
            .filterIsInstance<Media.Image>()
            .sortedByDescending { it.height * it.width }
            .map(::ImageViewFromFeature)
            .first()
        return Result.success(image)
    }

    override suspend fun getTrailer(): Result<VideoView> {
        val video = getDetail().media
            .asSequence()
            .filterIsInstance<Media.Video>()
            .map(::VideoViewFromFeature)
            .first()
        return Result.success(video)
    }

    override suspend fun getShowings(
        date: Date,
        latitude: Double,
        longitude: Double
    ): Result<List<CinemaBookingView>> {
        val items = event.getShowings(getDetail(), date, Location(latitude, longitude)).getOrThrow()
            .asSequence()
            .map { (cinema, showings) -> CinemaBookingViewFromFeature(cinema, showings) }
            .filter { it.availability.isNotEmpty() }
            .toList()
        return Result.success(items)
    }

    //

    private suspend fun getDetail(): MovieDetail = mutex.withLock {
        movie ?: event.getDetail(MovieFromId(id)).getOrThrow().also {
            movie = it
        }
    }

}