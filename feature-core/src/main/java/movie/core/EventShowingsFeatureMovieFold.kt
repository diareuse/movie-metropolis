package movie.core

import movie.core.Recoverable.Companion.foldCatching
import java.util.Date

class EventShowingsFeatureMovieFold(
    private vararg val options: EventShowingsFeature.Movie
) : EventShowingsFeature.Movie, Recoverable {

    override suspend fun get(date: Date, result: ResultCallback<CinemaWithShowings>) {
        options.foldCatching { option ->
            option.get(date) { result(it.onFailureThrow()) }
        }
    }

}