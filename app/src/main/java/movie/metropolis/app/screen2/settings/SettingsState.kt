package movie.metropolis.app.screen2.settings

data class SettingsState(
    val unseenOnly: Boolean = false,
    val moviesOnly: Boolean = false,
    val tickets: Boolean = false,
    val nearbyCinemas: String = "0",
    val selectedCalendar: String? = null
)