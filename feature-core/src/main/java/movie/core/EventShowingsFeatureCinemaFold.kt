package movie.core

import movie.core.Recoverable.Companion.foldCatching
import java.util.Date

class EventShowingsFeatureCinemaFold(
    private vararg val options: EventShowingsFeature.Cinema
) : EventShowingsFeature.Cinema, Recoverable {

    override suspend fun get(date: Date, result: ResultCallback<MovieWithShowings>) {
        options.foldCatching { option ->
            option.get(date) { result(it.onFailureThrow()) }
        }
    }

}