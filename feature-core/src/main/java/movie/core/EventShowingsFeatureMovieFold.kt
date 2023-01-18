package movie.core

import java.util.Date

class EventShowingsFeatureMovieFold(
    private vararg val options: EventShowingsFeature.Movie
) : EventShowingsFeature.Movie, Recoverable {

    override suspend fun get(date: Date, result: ResultCallback<CinemaWithShowings>) {
        var last: Throwable = NoSuchElementException()
        for (option in options) try {
            return option.get(date) { result(it.onFailureThrow()) }
        } catch (e: Throwable) {
            last = CompositeException(last, e)
            continue
        }
        throw last
    }

}