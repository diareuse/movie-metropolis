package movie.metropolis.app.presentation.ticket

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import movie.core.EventDetailFeature
import movie.core.EventShowingsFeature
import movie.core.adapter.MovieFromId
import movie.metropolis.app.model.DataFiltersView
import movie.metropolis.app.model.FiltersView
import movie.metropolis.app.model.LazyTimeView
import movie.metropolis.app.model.ProjectionType
import movie.metropolis.app.model.adapter.MovieDetailViewFromFeature
import java.util.Date
import java.util.Locale
import kotlin.math.max
import kotlin.time.Duration.Companion.days

class TicketFacadeMovieFromFeature(
    private val id: String,
    private val detail: EventDetailFeature,
    private val showings: EventShowingsFeature.Movie,
) : TicketFacade {

    private val activeLanguages = MutableStateFlow(setOf<Locale>())
    private val activeTypes = MutableStateFlow(setOf<ProjectionType>())
    private val _filters = MutableStateFlow(DataFiltersView())
    private val movie = flow {
        val detail = detail.get(MovieFromId(id)).getOrThrow()
        emit(detail)
    }.shareIn(GlobalScope, SharingStarted.Lazily, replay = 1)

    override val times: Flow<List<LazyTimeView>> = movie.map { detail ->
        val startTime = max(Date().time, detail.screeningFrom.time)
        val day = 1.days
        List(7) {
            val offset = (day * it).inWholeMilliseconds
            var out: LazyTimeView
            out = LazyTimeViewMovie(Date(startTime + offset), showings)
            out = LazyTimeViewCinemaUpdating(out) {
                _filters.update { _ -> it.toFiltersView() }
            }
            out
        }
    }

    override val poster: Flow<String?> = movie.map {
        MovieDetailViewFromFeature(it).poster?.url
    }
    override val name: Flow<String> = movie.map { it.name }
    override val filters = _filters.activate(activeLanguages, activeTypes)

    override fun toggle(language: FiltersView.Language) {
        activeLanguages.toggle(language.locale)
    }

    override fun toggle(type: FiltersView.Type) {
        activeTypes.toggle(type.type)
    }

}