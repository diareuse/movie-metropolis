package movie.metropolis.app.presentation.settings

import kotlinx.coroutines.flow.Flow
import movie.metropolis.app.model.Calendars

interface SettingsFacade {

    val filterSeen: Flow<Boolean>
    val onlyMovies: Flow<Boolean>
    val addToCalendar: Flow<Boolean>
    val clipRadius: Flow<Int>
    val selectedCalendar: Flow<String?>
    val filters: Flow<List<String>>

    suspend fun getCalendars(): Calendars
    fun setFilterSeen(value: Boolean)
    fun setOnlyMovies(value: Boolean)
    fun setClipRadius(value: Int)
    fun setSelectedCalendar(value: String?)
    fun setFilters(value: List<String>)

}