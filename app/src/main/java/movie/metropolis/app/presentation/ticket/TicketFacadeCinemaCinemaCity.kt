package movie.metropolis.app.presentation.ticket

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import movie.cinema.city.CinemaCity
import movie.metropolis.app.model.DataFiltersView
import movie.metropolis.app.model.FiltersView
import movie.metropolis.app.model.LazyTimeView
import movie.metropolis.app.model.ProjectionType
import movie.metropolis.app.model.adapter.CinemaViewFromCinema
import java.util.Date
import java.util.Locale
import kotlin.time.Duration.Companion.days

class TicketFacadeCinemaCinemaCity(
    private val id: String,
    private val cinemaCity: CinemaCity
) : TicketFacade {

    private val activeLanguages = MutableStateFlow(setOf<Locale>())
    private val activeTypes = MutableStateFlow(setOf<ProjectionType>())
    private val _filters = MutableStateFlow(DataFiltersView())
    private val cinema = flow { emit(cinemaCity.cinemas.getCinemas().first { it.id == id }) }
        .shareIn(GlobalScope, SharingStarted.Lazily, replay = 1)

    override val times: Flow<List<LazyTimeView>> = cinema.map { cinema ->
        val startTime = Date().time
        val day = 1.days
        List(7) {
            val offset = (day * it).inWholeMilliseconds
            var out: LazyTimeView
            out = LazyTimeViewCinema(cinema, Date(startTime + offset), cinemaCity)
            out = LazyTimeViewCinemaUpdating(out) {
                _filters.update { _ -> it.toFiltersView() }
            }
            out
        }
    }
    override val poster: Flow<String?> = cinema.map { it.image.toString() }
    override val name: Flow<String> = cinema.map { CinemaViewFromCinema(it).name }
    override val filters = _filters.activate(activeLanguages, activeTypes)

    override fun toggle(language: FiltersView.Language) {
        activeLanguages.toggle(language.locale)
    }

    override fun toggle(type: FiltersView.Type) {
        activeTypes.toggle(type.type)
    }

}