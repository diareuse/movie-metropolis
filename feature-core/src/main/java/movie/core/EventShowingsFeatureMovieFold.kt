package movie.core

import java.util.Date

class EventShowingsFeatureMovieFold(
    private vararg val options: EventShowingsFeature.Movie
) : EventShowingsFeature.Movie, Recoverable {

    override suspend fun get(date: Date): Result<CinemaWithShowings> {
        return options.fold { get(date) }
    }

}