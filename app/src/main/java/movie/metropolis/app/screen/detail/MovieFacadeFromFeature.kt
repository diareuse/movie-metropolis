package movie.metropolis.app.screen.detail

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import movie.core.EventFeature
import movie.core.FavoriteFeature
import movie.core.adapter.MovieFromId
import movie.core.model.Location
import movie.core.model.Media
import movie.core.model.MovieDetail
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.model.Filter
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.model.adapter.CinemaBookingViewFromFeature
import movie.metropolis.app.model.adapter.ImageViewFromFeature
import movie.metropolis.app.model.adapter.MovieDetailViewFromFeature
import movie.metropolis.app.model.adapter.VideoViewFromFeature
import movie.metropolis.app.screen.OnChangedListener
import movie.metropolis.app.screen.cinema.Listenable
import java.util.Date

class MovieFacadeFromFeature(
    private val id: String,
    private val event: EventFeature,
    private val favorite: FavoriteFeature
) : MovieFacade {

    private val mutex = Mutex(false)
    private var movie: MovieDetail? = null
    private val listenableFavorite = Listenable<OnChangedListener>()

    override suspend fun isFavorite(): Result<Boolean> {
        return favorite.isFavorite(getDetail())
    }

    override suspend fun getAvailableFrom(): Result<Date> {
        return Result.success(getDetail().screeningFrom.coerceAtLeast(Date()))
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

    override suspend fun toggleFavorite() {
        favorite.toggle(getDetail()).onSuccess {
            listenableFavorite.notify { onChanged() }
        }
    }

    override fun addOnFavoriteChangedListener(listener: OnChangedListener): OnChangedListener {
        listenableFavorite += listener
        return listener
    }

    override fun removeOnFavoriteChangedListener(listener: OnChangedListener) {
        listenableFavorite -= listener
    }

    override suspend fun getOptions(): Result<Map<Filter.Type, List<Filter>>> =
        Result.failure(NotImplementedError())

    override fun toggle(filter: Filter) = Unit
    override fun addOnChangedListener(listener: OnChangedListener) = listener
    override fun removeOnChangedListener(listener: OnChangedListener) = Unit

    //

    private suspend fun getDetail(): MovieDetail = mutex.withLock {
        movie ?: event.getDetail(MovieFromId(id)).getOrThrow().also {
            movie = it
        }
    }

}