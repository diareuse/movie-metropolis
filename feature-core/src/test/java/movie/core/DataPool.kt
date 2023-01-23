package movie.core

import movie.core.db.model.CinemaStored
import movie.core.db.model.MovieDetailView
import movie.core.db.model.MovieMediaStored
import movie.core.db.model.MovieMediaView
import movie.core.db.model.MoviePreviewView
import movie.core.db.model.MovieReferenceView
import movie.core.db.model.ShowingStored
import movie.core.model.Cinema
import movie.core.nwk.model.CinemaResponse
import movie.core.nwk.model.ConsentRemote
import movie.core.nwk.model.CustomerPointsResponse
import movie.core.nwk.model.CustomerResponse
import movie.core.nwk.model.EventResponse
import movie.core.nwk.model.ExtendedMovieResponse
import movie.core.nwk.model.ExtendedMovieResponse.Metadata
import movie.core.nwk.model.MovieDetailResponse
import movie.core.nwk.model.MovieEventResponse
import movie.core.nwk.model.MovieResponse
import org.mockito.kotlin.mock
import java.util.Date
import java.util.Locale
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

    object Cinemas : DataPool<Cinema> {

        override fun all(modifier: Modifier<Cinema>) = buildList {
            this += mock<Cinema> {
                on { id }.thenReturn("id")
            }
        }

    }

    object ExtendedMovieResponses : DataPool<ExtendedMovieResponse> {

        override fun all(modifier: Modifier<ExtendedMovieResponse>) = buildList {
            this += ExtendedMovieResponse(
                id = ExtendedMovieResponse.Key("3"),
                url = "",
                releasedAt = Date(),
                screeningFrom = Date(System.currentTimeMillis() + 5000),
                duration = 0.seconds,
                distributorCodes = emptyList(),
                media = listOf(ExtendedMovieResponse.Media.Image(0, 0, "")),
                metadata = mapOf(
                    Locale.getDefault() to Metadata("", null, null, null, null)
                )
            )
            this += ExtendedMovieResponse(
                id = ExtendedMovieResponse.Key("2"),
                url = "",
                releasedAt = Date(),
                screeningFrom = Date(System.currentTimeMillis() - 2000),
                duration = 0.seconds,
                distributorCodes = emptyList(),
                media = listOf(ExtendedMovieResponse.Media.Image(0, 0, "")),
                metadata = mapOf(
                    Locale.getDefault() to Metadata("", null, null, null, null)
                )
            )
            this += ExtendedMovieResponse(
                id = ExtendedMovieResponse.Key("1"),
                url = "",
                releasedAt = Date(),
                screeningFrom = Date(),
                duration = 0.seconds,
                distributorCodes = emptyList(),
                media = listOf(ExtendedMovieResponse.Media.Image(0, 0, "")),
                metadata = mapOf(
                    Locale.getDefault() to Metadata("", null, null, null, null)
                )
            )
        }.map(modifier)
    }

    object MoviePreviewViews : DataPool<MoviePreviewView> {

        override fun all(modifier: Modifier<MoviePreviewView>) = buildList {
            this += MoviePreviewView(
                id = "3",
                name = "",
                url = "",
                releasedAt = Date(),
                durationMillis = 0,
                screeningFrom = Date(System.currentTimeMillis() + 5000),
                description = "",
                directors = emptyList(),
                cast = emptyList(),
                countryOfOrigin = ""
            )
            this += MoviePreviewView(
                id = "2",
                name = "",
                url = "",
                releasedAt = Date(),
                durationMillis = 0,
                screeningFrom = Date(System.currentTimeMillis() - 2000),
                description = "",
                directors = emptyList(),
                cast = emptyList(),
                countryOfOrigin = ""
            )
            this += MoviePreviewView(
                id = "1",
                name = "",
                url = "",
                releasedAt = Date(),
                durationMillis = 0,
                screeningFrom = Date(),
                description = "",
                directors = emptyList(),
                cast = emptyList(),
                countryOfOrigin = ""
            )
        }.map(modifier)

    }

    object MovieMediaViews : DataPool<MovieMediaView> {

        override fun all(modifier: Modifier<MovieMediaView>) = buildList {
            this += MovieMediaView(0, 0, "", MovieMediaStored.Type.Image)
            this += MovieMediaView(0, 0, "", MovieMediaStored.Type.Image)
            this += MovieMediaView(0, 0, "", MovieMediaStored.Type.Image)
        }.map(modifier)

    }

    object MovieDetailResponses : DataPool<MovieDetailResponse> {

        override fun all(modifier: Modifier<MovieDetailResponse>) = buildList {
            this += MovieDetailResponse(
                id = "",
                name = "",
                nameOriginal = "",
                duration = 0.seconds,
                url = "",
                releasedAt = Date(),
                countryOfOrigin = null,
                cast = null,
                directors = "",
                synopsis = null,
                screeningFrom = Date(),
                genres = emptyList(),
                categories = emptyList(),
                screeningTags = emptyList(),
                restrictionUrl = "",
                media = listOf(MovieDetailResponse.Media.Image(0, 0, ""))
            )
        }.map(modifier)

    }

    object MovieDetailViews : DataPool<MovieDetailView> {

        override fun all(modifier: Modifier<MovieDetailView>) = buildList {
            this += MovieDetailView(
                id = "",
                name = "",
                url = "",
                releasedAt = Date(),
                durationMillis = 0,
                originalName = "",
                countryOfOrigin = null,
                cast = emptyList(),
                directors = emptyList(),
                description = "",
                screeningFrom = Date(),
                ageRestrictionUrl = "",
                rating = 0,
                linkImdb = null,
                linkRottenTomatoes = null,
                linkCsfd = null
            )
        }.map(modifier)

    }

    object CinemaResponses : DataPool<CinemaResponse> {

        override fun all(modifier: Modifier<CinemaResponse>) = buildList {
            this += CinemaResponse(
                postalCode = "",
                id = "666",
                addressLine = "",
                addressLine2 = null,
                latitude = 15.0,
                longitude = -15.0,
                description = "",
                name = "",
                city = "b"
            )
            this += CinemaResponse(
                postalCode = "",
                id = "3",
                addressLine = "",
                addressLine2 = null,
                latitude = 0.6,
                longitude = 0.6,
                description = "",
                name = "",
                city = "a"
            )
            this += CinemaResponse(
                postalCode = "",
                id = "1",
                addressLine = "",
                addressLine2 = null,
                latitude = 0.0,
                longitude = 0.0,
                description = "",
                name = "",
                city = "d"
            )
            this += CinemaResponse(
                postalCode = "",
                id = "2",
                addressLine = "",
                addressLine2 = null,
                latitude = 0.3,
                longitude = 0.3,
                description = "",
                name = "",
                city = "c"
            )
        }.map(modifier)

    }

    object CinemasStored : DataPool<CinemaStored> {

        override fun all(modifier: Modifier<CinemaStored>) = buildList {
            this += CinemaStored(
                id = "666",
                name = "",
                description = "",
                city = "d",
                address = emptyList(),
                latitude = 15.0,
                longitude = -15.0
            )
            this += CinemaStored(
                id = "3",
                name = "",
                description = "",
                city = "b",
                address = emptyList(),
                latitude = 0.6,
                longitude = 0.6
            )
            this += CinemaStored(
                id = "1",
                name = "",
                description = "",
                city = "c",
                address = emptyList(),
                latitude = 0.0,
                longitude = 0.0
            )
            this += CinemaStored(
                id = "2",
                name = "",
                description = "",
                city = "a",
                address = emptyList(),
                latitude = 0.3,
                longitude = 0.3
            )
        }.map(modifier)

    }

    object CustomerResponseCustomer : DataPool<CustomerResponse.Customer> {

        override fun all(modifier: Modifier<CustomerResponse.Customer>) = buildList {
            this += CustomerResponse.Customer(
                id = "",
                firstName = "",
                lastName = "",
                email = "",
                phone = "",
                birthAt = null,
                favoriteCinema = null,
                consent = ConsentRemote(marketing = false, marketingPremium = false),
                membership = CustomerResponse.Membership(null)
            )
        }.map(modifier)

    }

    object CustomerPointsResponses : DataPool<CustomerPointsResponse> {

        override fun all(modifier: Modifier<CustomerPointsResponse>) = buildList {
            this += CustomerPointsResponse(0.0, 0.0, Date())
        }.map(modifier)

    }

}