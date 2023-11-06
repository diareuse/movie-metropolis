package movie.metropolis.app.model

import kotlinx.coroutines.flow.Flow

interface LazyTimeView {
    val content: Flow<List<TimeView>>
}