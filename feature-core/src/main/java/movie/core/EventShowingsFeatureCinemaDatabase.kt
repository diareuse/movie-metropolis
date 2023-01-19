package movie.core

import movie.core.db.dao.MovieReferenceDao
import movie.core.db.dao.ShowingDao
import movie.core.model.Cinema
import movie.core.model.MovieReference
import movie.core.model.Showing
import movie.core.util.dayEnd
import movie.core.util.dayStart
import java.util.Date

class EventShowingsFeatureCinemaDatabase(
    private val showing: ShowingDao,
    private val reference: MovieReferenceDao,
    private val cinema: Cinema
) : EventShowingsFeature.Cinema {

    override suspend fun get(date: Date, result: ResultCallback<MovieWithShowings>) {
        val showings = showing.selectByCinema(
            rangeStart = date.dayStart.coerceAtLeast(Date()).time,
            rangeEnd = date.dayEnd.time,
            cinema = cinema.id
        )
        val movies = mutableMapOf<String, MovieReference>()
        val output = buildMap<MovieReference, MutableList<Showing>> {
            for (showing in showings) {
                val movieId = showing.movie.lowercase()
                val movie = movies.getOrPut(movieId) {
                    reference.select(movieId).let(::MovieReferenceFromDatabase)
                }
                val list = getOrPut(movie) { mutableListOf() }
                list.add(ShowingFromDatabase(showing, cinema))
            }
        }
        result(Result.success(output))
    }

}