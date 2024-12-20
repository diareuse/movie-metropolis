package movie.metropolis.app.model

import androidx.compose.runtime.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Stable
class LazyTimeView(
    val date: Date
) {

    val content = mutableStateListOf<TimeView>()
    val dateString by derivedStateOf { format.format(date).orEmpty() }
    val isEmpty by derivedStateOf { content.isEmpty() || content.all { it.filteredTimes.isEmpty() } }

    companion object {
        private val format get() = SimpleDateFormat("EEE\ndd\nMMM", Locale.getDefault())
    }

}