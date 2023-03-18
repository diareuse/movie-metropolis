package movie.metropolis.app.presentation.detail

import androidx.compose.ui.graphics.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import movie.core.EventDetailFeature
import movie.core.EventShowingsFeature
import movie.core.FavoriteFeature
import movie.core.ResultCallback
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
import movie.metropolis.app.model.adapter.MoviePreviewFromDetail
import movie.metropolis.app.model.adapter.VideoViewFromFeature
import movie.metropolis.app.presentation.Listenable
import movie.metropolis.app.presentation.OnChangedListener
import java.util.Date

class MovieFacadeFromFeature(
    private val id: String,
    private val showings: EventShowingsFeature.Factory,
    private val detail: EventDetailFeature,
    private val favorite: FavoriteFeature
) : MovieFacade {

    private val mutex = Mutex(false)
    private var movie: MovieDetail? = null
    private val listenableFavorite = Listenable<OnChangedListener>()

    override suspend fun isFavorite(): Result<Boolean> {
        return favorite.isFavorite(getDetail())
    }

    override suspend fun getAvailableFrom(callback: ResultCallback<Date>) {
        val detail = getDetail()
        val output = detail.screeningFrom.coerceAtLeast(Date())
        callback(Result.success(output))
    }

    override suspend fun getMovie(callback: ResultCallback<MovieDetailView>) {
        val detail = getDetail()
        val output = detail.let(::MovieDetailViewFromFeature)
        callback(Result.success(output))
    }

    override suspend fun getPoster(callback: ResultCallback<ImageView>) {
        val result = getDetail().media
            .asSequence()
            .filterIsInstance<Media.Image>()
            .sortedByDescending { it.height * it.width }
            .map { ImageViewFromFeature(it, Color.Black) }
            .first()
            .let(Result.Companion::success)
        callback(result)
    }

    override suspend fun getTrailer(callback: ResultCallback<VideoView>) {
        val detail = getDetail()
        val output = detail.media
            .asSequence()
            .filterIsInstance<Media.Video>()
            .map(::VideoViewFromFeature)
            .first()
        callback(Result.success(output))
    }

    override suspend fun getShowings(
        date: Date,
        latitude: Double,
        longitude: Double,
        callback: ResultCallback<List<CinemaBookingView>>
    ) {
        val detail = getDetail()
        val result = showings.movie(detail, Location(latitude, longitude)).get(date)
        val output = result.getOrThrow().asSequence()
            .map { (cinema, showings) -> CinemaBookingViewFromFeature(cinema, showings) }
            .filter { it.availability.isNotEmpty() }
            .toList()
        callback(Result.success(output))
    }

    override suspend fun toggleFavorite() {
        val movie = getDetail()
        val preview = MoviePreviewFromDetail(movie)
        favorite.toggle(preview).onSuccess {
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

    // ---

    private suspend fun getDetail(): MovieDetail {
        return movie ?: mutex.withLock {
            movie ?: detail.get(MovieFromId(id)).getOrThrow().also {
                movie = it
            }
        }
    }

}