package movie.core

import movie.core.adapter.CinemaWithDistance
import movie.core.model.Cinema
import movie.core.model.Location
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class EventCinemaFeatureDistance(
    private val origin: EventCinemaFeature
) : EventCinemaFeature {

    override suspend fun get(location: Location?, result: ResultCallback<Iterable<Cinema>>) {
        origin.get(location, result.map { cinemas ->
            when (location) {
                null -> cinemas
                else -> cinemas.map { cinema ->
                    CinemaWithDistance(cinema, distance(location, cinema.location))
                }
            }
        })
    }

    private fun distance(start: Location, end: Location): Double {
        var latitudeStart = start.latitude
        var latitudeEnd = end.latitude
        val deltaLatitude = Math.toRadians(latitudeEnd - latitudeStart)
        val deltaLongitude = Math.toRadians(end.longitude - start.longitude)

        latitudeStart = Math.toRadians(latitudeStart)
        latitudeEnd = Math.toRadians(latitudeEnd)

        val angleDifference = hav(deltaLatitude) +
                cos(latitudeStart) *
                cos(latitudeEnd) *
                hav(deltaLongitude)
        val distance = 2 * atan2(sqrt(angleDifference), sqrt(1 - angleDifference))

        return EarthRadius * distance
    }

    private fun hav(value: Double): Double {
        return sin(value / 2).pow(2.0)
    }

    companion object {
        private const val EarthRadius = 6371//km
    }

}