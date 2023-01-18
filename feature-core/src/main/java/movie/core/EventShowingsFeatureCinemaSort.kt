package movie.core

import java.util.Date

class EventShowingsFeatureCinemaSort(
    private val origin: EventShowingsFeature.Cinema
) : EventShowingsFeature.Cinema {

    override suspend fun get(date: Date, result: ResultCallback<MovieWithShowings>) {
        origin.get(date) {
            val output = it.map {
                it.entries
                    .asSequence()
                    .sortedByDescending { it.value.count() }
                    .map { it.key to it.value }
                    .toMap()
            }
            result(output)
        }
    }

}