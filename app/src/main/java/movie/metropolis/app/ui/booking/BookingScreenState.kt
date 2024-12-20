package movie.metropolis.app.ui.booking

import androidx.compose.runtime.*
import movie.metropolis.app.model.FiltersView
import movie.metropolis.app.model.LazyTimeView

@Stable
class BookingScreenState {

    var activeFilterCount by mutableIntStateOf(0)
    var poster by mutableStateOf(null as String?)
    var title by mutableStateOf("")
    var filters by mutableStateOf(FiltersView())
    val items = mutableStateListOf<LazyTimeView>()

}