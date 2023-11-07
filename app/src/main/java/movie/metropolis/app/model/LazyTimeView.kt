package movie.metropolis.app.model

import kotlinx.coroutines.flow.Flow
import java.util.Date

interface LazyTimeView {
    val date: Date
    val content: Flow<List<TimeView>>
}