package movie.core

import movie.core.model.Location
import java.util.Date
import movie.core.model.Cinema as CinemaModel
import movie.core.model.Movie as MovieModel

sealed interface EventShowingsFeature {

    interface Cinema : EventShowingsFeature {
        suspend fun get(date: Date, result: ResultCallback<MovieWithShowings>)
    }

    interface Movie : EventShowingsFeature {
        suspend fun get(date: Date, result: ResultCallback<CinemaWithShowings>)
    }

    interface Factory {
        fun cinema(cinema: CinemaModel): Cinema
        fun movie(movie: MovieModel, location: Location): Movie
    }

}