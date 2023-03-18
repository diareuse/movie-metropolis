package movie.core

import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.preference.EventPreference

class EventCinemaFeatureDistanceClosest(
    private val origin: EventCinemaFeature,
    private val preference: EventPreference
) : EventCinemaFeature {

    override suspend fun get(location: Location?): Result<Sequence<Cinema>> {
        return origin.get(location).map { cinemas ->
            cinemas.filter { cinema ->
                val distance = cinema.distance
                distance == null || distance.toInt() in 0 until preference.distanceKms
            }
        }
    }

}