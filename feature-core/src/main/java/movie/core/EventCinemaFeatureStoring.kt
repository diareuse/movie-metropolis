package movie.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import movie.core.adapter.asStored
import movie.core.db.dao.CinemaDao
import movie.core.model.Location

class EventCinemaFeatureStoring(
    private val origin: EventCinemaFeature,
    private val cinema: CinemaDao,
    private val effectScope: CoroutineScope
) : EventCinemaFeature {

    override suspend fun get(location: Location?) = origin.get(location).onSuccess { cinemas ->
        effectScope.launch {
            for (item in cinemas)
                cinema.insertOrUpdate(item.asStored())
        }
    }

}