package movie.core

import java.util.Date

class EventShowingsFeatureCinemaFold(
    private vararg val options: EventShowingsFeature.Cinema
) : EventShowingsFeature.Cinema, Recoverable {

    override suspend fun get(date: Date, result: ResultCallback<MovieWithShowings>) {
        var last: Throwable = NoSuchElementException()
        for (option in options) try {
            return option.get(date) { result(it.onFailureThrow()) }
        } catch (e: Throwable) {
            last = e
            continue
        }
        throw last
    }

}