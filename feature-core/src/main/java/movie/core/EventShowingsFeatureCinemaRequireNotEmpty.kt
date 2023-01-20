package movie.core

import movie.core.util.requireNotEmpty
import java.util.Date

class EventShowingsFeatureCinemaRequireNotEmpty(
    private val origin: EventShowingsFeature.Cinema
) : EventShowingsFeature.Cinema {

    override suspend fun get(date: Date, result: ResultCallback<MovieWithShowings>) {
        origin.get(date) {
            result(it.requireNotEmpty())
        }
    }

}

