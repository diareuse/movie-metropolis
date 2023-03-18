package movie.core

import movie.core.model.Cinema
import movie.core.model.Location

interface EventCinemaFeature {

    suspend fun get(location: Location?): Result<Sequence<Cinema>>

}