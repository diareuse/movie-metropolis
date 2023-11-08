package movie.metropolis.app.presentation.ticket

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import movie.metropolis.app.model.LazyTimeView
import movie.metropolis.app.model.TimeView

class LazyTimeViewCinemaUpdating(
    private val origin: LazyTimeView,
    private val updater: (List<TimeView>) -> Unit
) : LazyTimeView by origin {

    override val content: Flow<List<TimeView>>
        get() = origin.content.onEach(updater)

}