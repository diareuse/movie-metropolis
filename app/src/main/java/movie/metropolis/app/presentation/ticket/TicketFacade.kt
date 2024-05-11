package movie.metropolis.app.presentation.ticket

import android.location.Location
import kotlinx.coroutines.flow.Flow
import movie.metropolis.app.model.FiltersView
import movie.metropolis.app.model.LazyTimeView

interface TicketFacade {

    val times: Flow<List<LazyTimeView>>
    val poster: Flow<String?>
    val name: Flow<String>
    val filters: Flow<FiltersView>

    fun toggle(language: FiltersView.Language)
    fun toggle(type: FiltersView.Type)

    fun interface LocationFactory {
        fun create(location: Location?): TicketFacade
    }

    interface Factory {
        fun movie(id: String): LocationFactory
        fun cinema(id: String): LocationFactory
    }

}