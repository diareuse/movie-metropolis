package movie.metropolis.app.presentation.ticket

import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import movie.cinema.city.CinemaCity
import movie.metropolis.app.model.FiltersView
import movie.metropolis.app.model.LazyTimeView
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.ProjectionType
import movie.metropolis.app.model.ShowingTag
import movie.metropolis.app.model.TimeView
import movie.metropolis.app.model.adapter.CinemaViewFromCinema
import movie.metropolis.app.model.adapter.ImageViewFromMovie
import movie.metropolis.app.model.adapter.SpecificTimeViewFromFeature
import movie.metropolis.app.model.adapter.VideoViewFromFeature
import movie.metropolis.app.util.retryOnNetworkError
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds

class TicketFacadeCinemaCinemaCity(
    private val id: String,
    private val cinemaCity: CinemaCity
) : TicketFacade {

    private val cinema = flow { emit(cinemaCity.cinemas.getCinemas().first { it.id == id }) }
        .retryOnNetworkError()
        .shareIn(GlobalScope, SharingStarted.Lazily, replay = 1)

    private val dates = flow {
        val startTime = Date().time
        val day = 1.days
        val list = List(7) {
            val offset = (day * it).inWholeMilliseconds
            LazyTimeView(Date(startTime + offset))
        }
        emit(list)
    }

    override val filters = FiltersView()
    override val times = combineTransform(dates, cinema) { times, cinema ->
        emit(times)
        coroutineScope {
            for (t in times) launch {
                t.content += cinemaCity.events.getEvents(cinema, t.date)
                    .map { (ref, showings) ->
                        val m = MovieView(ref.id).apply {
                            val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
                            name = ref.name.localized
                            releasedAt = yearFormat.format(ref.releasedAt)
                            durationTime = ref.length ?: 0.seconds
                            availableFrom = releasedAt
                            poster = ref.images.first().let(::ImageViewFromMovie)
                            posterLarge = poster
                            video = ref.videos.firstOrNull()?.let(Any::toString)
                                ?.let(::VideoViewFromFeature)
                            url = ref.link.toString()
                        }
                        TimeView.Movie(m, filters).apply {
                            this.times += showings.groupBy {
                                ShowingTag(
                                    it.dubbing,
                                    it.subtitles,
                                    it.flags.map { ProjectionType(it.tag) }.toImmutableList()
                                )
                            }.mapValues { (_, items) ->
                                items.map(::SpecificTimeViewFromFeature)
                            }.apply {
                                filters.addAll(keys)
                            }
                        }
                    }
            }
        }
    }
    override val poster: Flow<String?> = cinema.map { it.image.toString() }
    override val name: Flow<String> = cinema.map { CinemaViewFromCinema(it).name }

}