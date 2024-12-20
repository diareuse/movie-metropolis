package movie.metropolis.app.presentation.ticket

import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import movie.cinema.city.CinemaCity
import movie.metropolis.app.model.FiltersView
import movie.metropolis.app.model.LazyTimeView
import movie.metropolis.app.model.ProjectionType
import movie.metropolis.app.model.ShowingTag
import movie.metropolis.app.model.TimeView
import movie.metropolis.app.model.adapter.CinemaViewFromCinema
import movie.metropolis.app.model.adapter.MovieDetailViewFromMovie
import movie.metropolis.app.model.adapter.SpecificTimeViewFromFeature
import movie.metropolis.app.util.retryOnNetworkError
import java.util.Date
import kotlin.math.max
import kotlin.time.Duration.Companion.days

class TicketFacadeMovieCinemaCity(
    private val id: String,
    private val cinema: CinemaCity
) : TicketFacade {

    private val movie = flow { emit(cinema.events.getEvent(id)) }
        .retryOnNetworkError()
        .shareIn(GlobalScope, SharingStarted.Lazily, replay = 1)

    override val filters = FiltersView()
    override val times: Flow<List<LazyTimeView>> = movie.transform { detail ->
        val startTime = max(Date().time, detail.screeningFrom.time)
        val day = 1.days
        val times = List(7) {
            val offset = (day * it).inWholeMilliseconds
            LazyTimeView(Date(startTime + offset))
        }
        emit(times)
        coroutineScope {
            val cinemas = cinema.cinemas.getCinemas()
            for (t in times) launch {
                for (it in cinemas) launch cinema@{
                    val showings = cinema.events.getEvents(it, t.date)
                        .filterKeys { it.id == id }.values.flatten()
                    if (showings.isEmpty()) return@cinema
                    t.content += TimeView.Cinema(CinemaViewFromCinema(it), filters).apply {
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

    override val poster: Flow<String?> = movie.map {
        MovieDetailViewFromMovie(it).backdrop?.url
    }
    override val name: Flow<String> = movie.map { it.name.localized }

}

fun FiltersView.addAll(tags: Iterable<ShowingTag>) {
    for ((dubbing, subs, types) in tags) {
        if (languages.none { it.locale == dubbing })
            languages.add(FiltersView.Language(dubbing))
        if (subs != null && languages.none { it.locale == subs })
            languages.add(FiltersView.Language(subs))
        for (type in types) if (this.types.none { it.type == type })
            this.types.add(FiltersView.Type(type))
    }
}