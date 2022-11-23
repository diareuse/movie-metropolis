package movie.metropolis.app.screen.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import movie.metropolis.app.feature.global.EventFeature
import movie.metropolis.app.feature.global.Media
import movie.metropolis.app.feature.global.MoviePreview
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.asLoadable
import movie.metropolis.app.screen.map
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import kotlin.time.Duration

@HiltViewModel
class ListingViewModel @Inject constructor(
    private val event: EventFeature
) : ViewModel() {

    val current = flow { emit(event.getCurrent().asLoadable()) }
        .map { it.map { it.map(::MovieViewFromFeature) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())
    val upcoming = flow { emit(event.getUpcoming().asLoadable()) }
        .map { it.map { it.map(::MovieViewFromFeature) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())

}

data class MovieViewFromFeature(
    private val movie: MoviePreview
) : MovieView {

    private val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    private val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)

    override val id: String
        get() = movie.id
    override val name: String
        get() = movie.name
    override val releasedAt: String
        get() = yearFormat.format(movie.releasedAt)
    override val duration: String
        get() = movie.duration.toStringComponents()
    override val availableFrom: String
        get() = dateFormat.format(movie.screeningFrom)
    override val directors: List<String>
        get() = movie.directors.toList()
    override val cast: List<String>
        get() = movie.cast.toList()
    override val countryOfOrigin: String
        get() = movie.countryOfOrigin
    override val poster: ImageView?
        get() = movie.media
            .asSequence()
            .filterIsInstance<Media.Image>()
            .sortedByDescending { it.height * it.width }
            .firstOrNull()
            ?.let(::ImageViewFromFeature)
    override val video: VideoView?
        get() = movie.media
            .asSequence()
            .filterIsInstance<Media.Video>()
            .firstOrNull()
            ?.let(::VideoViewFromFeature)
}

data class VideoViewFromFeature(
    private val feature: Media.Video
) : VideoView {

    override val url: String
        get() = feature.url
}

data class ImageViewFromFeature(
    private val feature: Media.Image
) : ImageView {
    override val aspectRatio: Float
        get() = feature.aspectRatio
    override val url: String
        get() = feature.url
}

fun Duration.toStringComponents() = toComponents { hours, minutes, _, _ ->
    when {
        hours <= 0 -> "%d".format(minutes)
        else -> "%dh %dm".format(hours, minutes)
    }
}