package movie.metropolis.app.presentation.detail

import androidx.compose.ui.graphics.*
import kotlinx.coroutines.sync.Mutex
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
        var movie = movie
        getDetail { movie = it.getOrNull() }
        return favorite.isFavorite(movie ?: return Result.failure(IllegalStateException()))
    }

    override suspend fun getAvailableFrom(callback: ResultCallback<Date>) {
        getDetail { result ->
            val output = result.map { it.screeningFrom.coerceAtLeast(Date()) }
            callback(output)
        }
    }

    override suspend fun getMovie(callback: ResultCallback<MovieDetailView>) {
        getDetail { result ->
            val output = result.map(::MovieDetailViewFromFeature)
            callback(output)
        }
    }

    override suspend fun getPoster(callback: ResultCallback<ImageView>) {
        getDetail { result ->
            val output = result.map { detail ->
                detail.media
                    .asSequence()
                    .filterIsInstance<Media.Image>()
                    .sortedByDescending { it.height * it.width }
                    .map { ImageViewFromFeature(it, Color(detail.spotColor)) }
                    .first()
            }
            callback(output)
        }
    }

    override suspend fun getTrailer(callback: ResultCallback<VideoView>) {
        getDetail { result ->
            val output = result.map { detail ->
                detail.media
                    .asSequence()
                    .filterIsInstance<Media.Video>()
                    .map(::VideoViewFromFeature)
                    .first()
            }
            callback(output)
        }
    }

    override suspend fun getShowings(
        date: Date,
        latitude: Double,
        longitude: Double,
        callback: ResultCallback<List<CinemaBookingView>>
    ) {
        getDetail { detail ->
            showings.movie(detail.getOrThrow(), Location(latitude, longitude)).get(date) {
                val result = it.getOrThrow().asSequence()
                    .map { (cinema, showings) -> CinemaBookingViewFromFeature(cinema, showings) }
                    .filter { it.availability.isNotEmpty() }
                    .toList()
                callback(Result.success(result))
            }
        }
    }

    override suspend fun toggleFavorite() {
        var movie = movie
        getDetail { movie = it.getOrNull() }
        val preview = MoviePreviewFromDetail(requireNotNull(movie))
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

    private suspend fun getDetail(callback: ResultCallback<MovieDetail>) {
        val movie = movie
        if (movie != null)
            return callback(Result.success(movie))
        mutex.lock()
        val movieLocked = this.movie
        if (movieLocked != null) {
            mutex.unlock()
            return callback(Result.success(movieLocked))
        }
        detail.get(MovieFromId(id)) {
            if (mutex.isLocked)
                mutex.unlock()
            callback(it.onSuccess {
                this.movie = it
            })
        }
    }

}