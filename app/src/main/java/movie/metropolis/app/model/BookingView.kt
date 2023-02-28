package movie.metropolis.app.model

import androidx.compose.runtime.*

@Immutable
sealed interface BookingView {

    val id: String
    val name: String
    val date: String
    val time: String
    val isPaid: Boolean
    val movie: MovieDetailView
    val cinema: CinemaView

    @Immutable
    object Loading : BookingView {
        override val id: String
            get() = throw IllegalStateException("No Data")
        override val name: String
            get() = throw IllegalStateException("No Data")
        override val date: String
            get() = throw IllegalStateException("No Data")
        override val time: String
            get() = throw IllegalStateException("No Data")
        override val isPaid: Boolean
            get() = throw IllegalStateException("No Data")
        override val movie: MovieDetailView
            get() = throw IllegalStateException("No Data")
        override val cinema: CinemaView
            get() = throw IllegalStateException("No Data")
    }

    @Immutable
    object Error : BookingView {
        override val id: String
            get() = throw IllegalStateException("No Data")
        override val name: String
            get() = throw IllegalStateException("No Data")
        override val date: String
            get() = throw IllegalStateException("No Data")
        override val time: String
            get() = throw IllegalStateException("No Data")
        override val isPaid: Boolean
            get() = throw IllegalStateException("No Data")
        override val movie: MovieDetailView
            get() = throw IllegalStateException("No Data")
        override val cinema: CinemaView
            get() = throw IllegalStateException("No Data")
    }

    @Immutable
    interface Expired : BookingView

    @Immutable
    interface Active : BookingView {

        val hall: String
        val seats: List<Seat>

        @Immutable
        interface Seat {
            val row: String
            val seat: String
        }

    }

}