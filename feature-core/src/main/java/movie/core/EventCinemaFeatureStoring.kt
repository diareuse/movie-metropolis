package movie.core

import movie.core.adapter.asStored
import movie.core.db.dao.CinemaDao
import movie.core.model.Cinema
import movie.core.model.Location

class EventCinemaFeatureStoring(
    private val origin: EventCinemaFeature,
    private val cinema: CinemaDao
) : EventCinemaFeature {

    override suspend fun get(location: Location?, result: ResultCallback<Iterable<Cinema>>) {
        origin.get(location, result.then { cinemas ->
            for (item in cinemas)
                cinema.insertOrUpdate(item.asStored())
        })
    }

}