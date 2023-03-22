package movie.core

import java.util.Date

class EventShowingsFeatureCinemaSort(
    private val origin: EventShowingsFeature.Cinema
) : EventShowingsFeature.Cinema {

    override suspend fun get(date: Date): Result<MovieWithShowings> {
        return origin.get(date).mapCatching {
            it.entries
                .asSequence()
                .sortedByDescending { it.value.count() }
                .map { it.key to it.value }
                .toMap()
        }
    }

}