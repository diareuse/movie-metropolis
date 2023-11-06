package movie.metropolis.app.model

sealed interface TimeView {

    val times: Map<ShowingTag, List<SpecificTimeView>>

    interface Cinema : TimeView {
        val cinema: CinemaView
    }

    interface Movie : TimeView {
        val movie: MovieView
    }

}