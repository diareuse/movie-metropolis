package movie.core

import movie.core.adapter.CinemaFromDatabase
import movie.core.db.dao.CinemaDao
import movie.core.model.Cinema
import movie.core.model.Location

class EventCinemaFeatureDatabase(
    private val cinema: CinemaDao
) : EventCinemaFeature {

    override suspend fun get(location: Location?, result: ResultCallback<Iterable<Cinema>>) {
        val data = cinema.selectAll().map(::CinemaFromDatabase)
        result(Result.success(data))
    }

}