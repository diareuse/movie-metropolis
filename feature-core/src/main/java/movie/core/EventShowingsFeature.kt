package movie.core

import movie.core.model.Location
import java.util.Date
import movie.core.model.Cinema as CinemaModel
import movie.core.model.Movie as MovieModel

sealed interface EventShowingsFeature {

    interface Cinema : EventShowingsFeature {
        suspend fun get(date: Date, result: ResultCallback<MovieWithShowings>)

        companion object {
            suspend fun Cinema.get(date: Date): Result<MovieWithShowings> {
                var out: Result<MovieWithShowings> = Result.failure(IllegalStateException())
                get(date) { out = it }
                return out
            }
        }
    }

    interface Movie : EventShowingsFeature {
        suspend fun get(date: Date, result: ResultCallback<CinemaWithShowings>)

        companion object {
            suspend fun Movie.get(date: Date): Result<CinemaWithShowings> {
                var out: Result<CinemaWithShowings> = Result.failure(IllegalStateException())
                get(date) { out = it }
                return out
            }
        }
    }

    interface Factory {
        fun cinema(cinema: CinemaModel): Cinema
        fun movie(movie: MovieModel, location: Location): Movie
    }

}