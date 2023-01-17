package movie.core

import movie.core.model.Location
import java.util.Date

sealed interface EventPreviewFeature {

    interface Cinema : EventPreviewFeature {
        suspend fun get(date: Date, result: ResultCallback<MovieWithShowings>)
    }

    interface Movie : EventPreviewFeature {
        suspend fun get(date: Date, result: ResultCallback<CinemaWithShowings>)
    }

    interface Factory {
        fun cinema(cinema: Cinema): Cinema
        fun movie(movie: Movie, location: Location): Movie
    }

}