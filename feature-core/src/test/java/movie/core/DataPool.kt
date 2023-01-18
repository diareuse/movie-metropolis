package movie.core

import movie.core.db.model.MovieReferenceView
import movie.core.db.model.ShowingStored
import movie.core.nwk.model.EventResponse
import movie.core.nwk.model.MovieEventResponse
import movie.core.nwk.model.MovieResponse
import java.util.Date
import kotlin.time.Duration.Companion.seconds

typealias Modifier<T> = (T) -> T

interface DataPool<T> {

    fun all(modifier: Modifier<T> = { it }): List<T>
    fun first(modifier: Modifier<T> = { it }) = all(modifier).first()

    object MovieEventResponses : DataPool<MovieEventResponse> {

        override fun all(modifier: Modifier<MovieEventResponse>) = buildList {
            this += MovieEventResponse(
                movies = MovieResponses.all(),
                events = EventResponses.all()
            )
        }.map(modifier)

    }

    object MovieResponses : DataPool<MovieResponse> {

        override fun all(modifier: Modifier<MovieResponse>) = buildList {
            this += MovieResponse(
                id = "a",
                name = "",
                duration = 0.seconds,
                posterUrl = "",
                videoUrl = null,
                url = "",
                releasedAt = Date(0),
                tags = emptyList()
            )
            this += MovieResponse(
                id = "b",
                name = "",
                duration = 0.seconds,
                posterUrl = "",
                videoUrl = null,
                url = "",
                releasedAt = Date(0),
                tags = emptyList()
            )
        }.map(modifier)

    }

    object EventResponses : DataPool<EventResponse> {

        override fun all(modifier: Modifier<EventResponse>) = buildList {
            this += EventResponse(
                id = "a",
                movieId = "a",
                cinemaId = "",
                startsAt = Date(0),
                bookingUrl = "",
                tags = emptyList(),
                soldOut = false,
                auditorium = ""
            )
            this += EventResponse(
                id = "aa",
                movieId = "a",
                cinemaId = "",
                startsAt = Date(0),
                bookingUrl = "",
                tags = emptyList(),
                soldOut = false,
                auditorium = ""
            )
            this += EventResponse(
                id = "b",
                movieId = "b",
                cinemaId = "",
                startsAt = Date(0),
                bookingUrl = "",
                tags = emptyList(),
                soldOut = false,
                auditorium = ""
            )
            this += EventResponse(
                id = "bb",
                movieId = "b",
                cinemaId = "",
                startsAt = Date(0),
                bookingUrl = "",
                tags = emptyList(),
                soldOut = false,
                auditorium = ""
            )
            this += EventResponse(
                id = "bbb",
                movieId = "b",
                cinemaId = "",
                startsAt = Date(0),
                bookingUrl = "",
                tags = emptyList(),
                soldOut = false,
                auditorium = ""
            )
        }.map(modifier)

    }

    object ShowingsStored : DataPool<ShowingStored> {

        override fun all(modifier: Modifier<ShowingStored>) = buildList {
            this += ShowingStored(
                id = "",
                cinema = "",
                startsAt = Date(0),
                bookingUrl = "",
                isEnabled = true,
                auditorium = "",
                language = "",
                types = emptyList(),
                movie = ""
            )
        }.map(modifier)

    }

    object MovieReferenceViews : DataPool<MovieReferenceView> {

        override fun all(modifier: Modifier<MovieReferenceView>) = buildList {
            this += MovieReferenceView(
                id = "",
                name = "",
                url = "",
                releasedAt = Date(0),
                durationMillis = 0,
                poster = "",
                video = null
            )
        }.map(modifier)

    }

}