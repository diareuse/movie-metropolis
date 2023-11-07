package movie.metropolis.app.model

import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface LazyTimeView {

    val date: Date
    val content: Flow<List<TimeView>>
    val dateString get() = format.format(date)

    companion object {
        private val format = SimpleDateFormat("dd\nMMM", Locale.getDefault())
    }

}