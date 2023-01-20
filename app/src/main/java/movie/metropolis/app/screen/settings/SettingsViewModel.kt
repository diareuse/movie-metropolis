package movie.metropolis.app.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import movie.metropolis.app.screen.retainStateIn
import movie.metropolis.app.screen.settings.SettingsFacade.Companion.addToCalendarFlow
import movie.metropolis.app.screen.settings.SettingsFacade.Companion.calendarFlow
import movie.metropolis.app.screen.settings.SettingsFacade.Companion.clipRadiusFlow
import movie.metropolis.app.screen.settings.SettingsFacade.Companion.filterSeenFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val facade: SettingsFacade
) : ViewModel() {

    val filterSeen = facade.filterSeenFlow
        .retainStateIn(viewModelScope, false)

    val addToCalendar = facade.addToCalendarFlow
        .retainStateIn(viewModelScope, false)

    val calendars = facade.calendarFlow
        .retainStateIn(viewModelScope, emptyMap())

    val clipRadius = facade.clipRadiusFlow
        .retainStateIn(viewModelScope, 0)

    fun updateFilterSeen(value: Boolean) {
        facade.filterSeen = value
    }

    fun updateCalendar(id: String?) {
        facade.selectCalendar(id)
    }

    fun updateClipRadius(value: Int) {
        facade.clipRadius = value
    }

}