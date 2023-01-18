package movie.core

import java.util.Date

class EventShowingsFeatureCinemaCatch(
    private val origin: EventShowingsFeature.Cinema
) : EventShowingsFeature.Cinema, Recoverable {

    override suspend fun get(date: Date, result: ResultCallback<MovieWithShowings>) {
        runCatchingResult(result) {
            origin.get(date, it)
        }
    }

}