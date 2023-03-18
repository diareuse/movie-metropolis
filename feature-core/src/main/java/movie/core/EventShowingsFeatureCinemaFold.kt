package movie.core

import java.util.Date

class EventShowingsFeatureCinemaFold(
    private vararg val options: EventShowingsFeature.Cinema
) : EventShowingsFeature.Cinema, Recoverable {

    override suspend fun get(date: Date): Result<MovieWithShowings> {
        return options.fold { get(date) }
    }

}