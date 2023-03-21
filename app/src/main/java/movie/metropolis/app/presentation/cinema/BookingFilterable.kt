package movie.metropolis.app.presentation.cinema

import kotlinx.coroutines.flow.Flow
import movie.metropolis.app.model.Filter

interface BookingFilterable {

    val options: Flow<Map<Filter.Type, List<Filter>>>

    fun toggle(filter: Filter)

}