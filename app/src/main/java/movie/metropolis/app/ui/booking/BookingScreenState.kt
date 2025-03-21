package movie.metropolis.app.ui.booking

import androidx.compose.runtime.*
import movie.metropolis.app.model.FiltersView
import movie.metropolis.app.model.LazyTimeView
import kotlin.time.Duration.Companion.seconds

@Stable
class BookingScreenState {

    var activeFilterCount by mutableIntStateOf(0)
    var poster by mutableStateOf(null as String?)
    var title by mutableStateOf("")
    var filters by mutableStateOf(FiltersView())
    var duration by mutableStateOf(0.seconds)
    val items = mutableStateListOf<LazyTimeView>()
    var selectedIndex by mutableIntStateOf(0)

    fun selectFirstNonEmpty() {
        if (selectedIndex == 0) {
            val index = items.indexOfFirst { !it.isEmpty }
            if (index == -1) return
            selectedIndex = index
        }
    }

}