package movie.metropolis.app.ui.home.component

import androidx.compose.runtime.*
import androidx.compose.ui.platform.*
import movie.metropolis.app.R
import java.util.Calendar
import kotlin.time.Duration.Companion.hours

@Composable
fun rememberTimeOfDayString(name: String): String {
    val context = LocalContext.current
    return remember(context, System.currentTimeMillis() / 1.hours.inWholeMilliseconds) {
        val calendar = Calendar.getInstance()
        val res = when (calendar.get(Calendar.HOUR_OF_DAY)) {
            in 5..<12 -> R.string.time_of_day_intro_morning
            in 12..13 -> R.string.time_of_day_intro_noon
            in 14..19 -> R.string.time_of_day_intro_afternoon
            else -> R.string.time_of_day_intro_evening
        }
        context.getString(res, name)
    }
}