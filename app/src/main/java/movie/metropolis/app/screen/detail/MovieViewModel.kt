package movie.metropolis.app.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import movie.metropolis.app.feature.global.EventFeature
import movie.metropolis.app.feature.global.Media
import movie.metropolis.app.feature.global.Movie
import movie.metropolis.app.feature.global.MovieDetail
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.asLoadable
import movie.metropolis.app.screen.listing.ImageViewFromFeature
import movie.metropolis.app.screen.listing.VideoViewFromFeature
import movie.metropolis.app.screen.listing.toStringComponents
import movie.metropolis.app.screen.map
import movie.metropolis.app.screen.mapNotNull
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MovieViewModel @Inject constructor(
    handle: SavedStateHandle,
    private val event: EventFeature
) : ViewModel() {

    private val id = handle.get<String>("movie").orEmpty()
    private val movieDetail = flow { emit(event.getDetail(MovieFromId(id))) }
        .map { it.asLoadable() }
        .shareIn(viewModelScope, SharingStarted.Lazily)

    val detail = movieDetail
        .map { it.map(::MovieDetailViewFromFeature) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())

    val trailer: Flow<Loadable<VideoView>> = movieDetail.map {
        it.mapNotNull {
            it.media
                .asSequence()
                .filterIsInstance<Media.Video>()
                .firstOrNull()
                ?.let(::VideoViewFromFeature)
        }
    }
    val poster: Flow<Loadable<ImageView>> = movieDetail.map {
        it.mapNotNull {
            it.media
                .asSequence()
                .filterIsInstance<Media.Image>()
                .sortedByDescending { it.height * it.width }
                .firstOrNull()
                ?.let(::ImageViewFromFeature)
        }
    }

    // todo add listing showings by date

}

data class MovieDetailViewFromFeature(
    private val movie: MovieDetail
) : MovieDetailView {

    private val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    private val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)

    override val name: String
        get() = movie.name
    override val nameOriginal: String
        get() = movie.originalName
    override val releasedAt: String
        get() = yearFormat.format(movie.releasedAt)
    override val duration: String
        get() = movie.duration.toStringComponents()
    override val countryOfOrigin: String
        get() = movie.countryOfOrigin
    override val cast: List<String>
        get() = movie.cast.toList()
    override val directors: List<String>
        get() = movie.directors.toList()
    override val description: String
        get() = movie.description
    override val availableFrom: String
        get() = dateFormat.format(movie.screeningFrom)
}

data class MovieFromId(
    override val id: String
) : Movie {

    override val name: String = ""
    override val url: String = ""
    override val releasedAt: Date = Date(0)
    override val duration: Duration = 0.seconds

}