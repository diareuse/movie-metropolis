package movie.core

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import movie.core.adapter.MovieFromId
import movie.core.db.dao.BookingDao
import movie.core.db.dao.CinemaDao
import movie.core.db.dao.MovieDao
import movie.core.db.dao.MovieDetailDao
import movie.core.db.dao.MovieMediaDao
import movie.core.db.dao.MoviePreviewDao
import movie.core.db.dao.MovieRatingDao
import movie.core.db.dao.MovieReferenceDao
import movie.core.db.dao.ShowingDao
import movie.core.db.model.BookingStored
import movie.core.di.EventFeatureModule
import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.nwk.PerformanceTracerNoop
import movie.core.nwk.di.NetworkModule
import movie.core.preference.EventPreference
import movie.image.ImageAnalyzer
import movie.image.Swatch
import movie.image.SwatchColor
import movie.rating.LinkProvider
import movie.rating.RatingProvider
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.Date
import kotlin.math.abs
import kotlin.test.assertEquals
import kotlin.test.assertFails

class EventFeatureTest : FeatureTest() {

    private lateinit var analyzer: ImageAnalyzer
    private lateinit var rating: RatingProvider
    private lateinit var link: LinkProvider
    private lateinit var ratingDao: MovieRatingDao
    private lateinit var preference: EventPreference
    private lateinit var bookingDao: BookingDao
    private lateinit var cinema: Cinema
    private lateinit var feature: EventFeature
    private lateinit var showingDao: ShowingDao
    private lateinit var cinemaDao: CinemaDao
    private lateinit var detailDao: MovieDetailDao
    private lateinit var mediaDao: MovieMediaDao
    private lateinit var referenceDao: MovieReferenceDao
    private lateinit var previewDao: MoviePreviewDao
    private lateinit var movieDao: MovieDao

    override fun prepare() {
        val module = EventFeatureModule()
        val service = NetworkModule()
        showingDao = mock()
        cinemaDao = mock()
        detailDao = mock()
        mediaDao = mock()
        referenceDao = mock()
        previewDao = mock()
        bookingDao = mock()
        movieDao = mock()
        ratingDao = mock()
        preference = mock()
        analyzer = mock {
            on { runBlocking { getColors(any()) } }.thenReturn(
                Swatch(
                    SwatchColor.Black,
                    SwatchColor.Black,
                    SwatchColor.Black
                )
            )
        }
        rating = mock {
            on { runBlocking { getRating(any()) } }.thenReturn(0)
        }
        link = mock()
        whenever(preference.filterSeen).thenReturn(false)
        feature = module.featureSaving(
            service.event(clientData, PerformanceTracerNoop),
            service.cinema(clientRoot, PerformanceTracerNoop),
            showingDao,
            cinemaDao,
            detailDao,
            mediaDao,
            referenceDao,
            previewDao,
            movieDao,
            ratingDao,
            rating,
            link, link, link
        )
        feature = module.feature(
            showingDao,
            cinemaDao,
            detailDao,
            mediaDao,
            referenceDao,
            previewDao,
            bookingDao,
            preference,
            feature,
            analyzer
        )
        cinema = mock()
        whenever(cinema.id).thenReturn("cinema")
    }

    // ---

    @Test
    fun getShowings_cinema_responds_withSuccess() = runTest {
        prepareShowingsResponse()
        val result = feature.getShowings(cinema, Date(0)).getOrThrow()
        assertEquals(16, result.size)
    }

    @Test
    fun getShowings_cinema_responds_withFailure() = runTest {
        responder.on(UrlResponder.EventOccurrence("cinema", "1970-01-01")) {
            method = HttpMethod.Get
            code = HttpStatusCode.BadRequest
            body = ""
        }
        assertFails {
            feature.getShowings(cinema, Date(0)).getOrThrow()
        }
    }

    @Test
    fun getShowings_movie_responds_withSuccess() = runTest {
        prepareShowingsResponse("1056")
        prepareShowingsResponse("1052")
        prepareShowingsResponse("1033")
        prepareShowingsResponse("1031")
        prepareShowingsResponse("1030")
        prepareShowingsResponse("1051")
        responder.on(UrlResponder.CinemaLocation(0.0, 0.0)) {
            fileAsBody("data-api-service-cinema-bylocation.json")
        }
        responder.on(UrlResponder.Cinema) {
            fileAsBody("cinemas.json")
        }
        val value =
            feature.getShowings(MovieFromId("5376o2R"), Date(0), Location(0.0, 0.0)).getOrThrow()
        assertEquals(6, value.size)
        assertEquals(1, value.entries.first { it.key.id == "1031" }.value.toList().size)
    }

    @Test
    fun getShowings_movie_responds_withSuccessEmpty() = runTest {
        responder.on(UrlResponder.CinemaLocation(0.0, 0.0)) {
            fileAsBody("data-api-service-cinema-bylocation.json")
        }
        responder.on(UrlResponder.Cinema) {
            fileAsBody("cinemas.json")
        }
        val value =
            feature.getShowings(MovieFromId("5376o2R"), Date(0), Location(0.0, 0.0)).getOrThrow()
        assertEquals(0, value.entries.sumOf { it.value.count() })
    }

    @Test
    fun getCinemas_responds_withSuccess_nullLocation() = runTest {
        responder.on(UrlResponder.Cinema) {
            fileAsBody("cinemas.json")
        }
        val value = feature.getCinemas(null).getOrThrow()
        assertEquals(13, value.count())
    }

    @Test
    fun getCinemas_responds_withFailure_nullLocation() = runTest {
        responder.on(UrlResponder.Cinema) {
            code = HttpStatusCode.BadRequest
            body = ""
        }
        assertFails {
            feature.getCinemas(null).getOrThrow()
        }
    }

    @Test
    fun getCinemas_responds_withSuccess() = runTest {
        responder.on(UrlResponder.CinemaLocation(0.0, 0.0)) {
            fileAsBody("data-api-service-cinema-bylocation.json")
        }
        responder.on(UrlResponder.Cinema) {
            fileAsBody("cinemas.json")
        }
        val value = feature.getCinemas(Location(0.0, 0.0)).getOrThrow()
        assertEquals(6, value.count())
    }

    @Test
    fun getCinemas_responds_withFailure() = runTest {
        responder.on(UrlResponder.CinemaLocation(0.0, 0.0)) {
            code = HttpStatusCode.BadRequest
            body = ""
        }
        assertFails {
            feature.getCinemas(Location(0.0, 0.0)).getOrThrow()
        }
    }

    @Test
    fun getDetail_responds_withSuccess() = runTest {
        val id = "5376O2R"
        responder.on(UrlResponder.Detail(id)) {
            fileAsBody("data-api-service-films-byDistributorCode.json")
        }
        feature.getDetail(MovieFromId(id)).getOrThrow()
    }

    @Test
    fun getDetail_responds_withFailure() = runTest {
        val id = "5376O2R"
        responder.on(UrlResponder.Detail(id)) {
            code = HttpStatusCode.BadRequest
            body = ""
        }
        assertFails {
            feature.getDetail(MovieFromId(id)).getOrThrow()
        }
    }

    @Test
    fun getCurrent_responds_withSuccess() = runTest {
        responder.on(UrlResponder.MoviesByShowing("SHOWING")) {
            fileAsBody("data-api-service-films-by-showing-type-SHOWING.json")
        }
        val value = feature.getCurrent().getOrThrow()
        assertEquals(34, value.count())
    }

    @Test
    fun getCurrent_responds_withFailure() = runTest {
        responder.on(UrlResponder.MoviesByShowing("SHOWING")) {
            code = HttpStatusCode.BadRequest
            body = ""
        }
        assertFails {
            feature.getCurrent().getOrThrow()
        }
    }

    @Test
    fun getCurrent_sortsBy_closestReleases() = runTest {
        responder.on(UrlResponder.MoviesByShowing("SHOWING")) {
            fileAsBody("data-api-service-films-by-showing-type-SHOWING.json")
        }
        val value = feature.getCurrent().getOrThrow()
            .map { abs(Date().time - it.screeningFrom.time) }
        value.reduce { acc, l ->
            assert(acc <= l)
            l
        }
    }

    @Test
    fun getUpcoming_responds_withSuccess() = runTest {
        responder.on(UrlResponder.MoviesByShowing("FUTURE")) {
            fileAsBody("data-api-service-films-by-showing-type-FUTURE.json")
        }
        val value = feature.getUpcoming().getOrThrow()
        assertEquals(18, value.count())
    }

    @Test
    fun getUpcoming_responds_withFailure() = runTest {
        responder.on(UrlResponder.MoviesByShowing("FUTURE")) {
            code = HttpStatusCode.BadRequest
            body = ""
        }
        assertFails {
            feature.getUpcoming().getOrThrow()
        }
    }

    @Test
    fun getUpcoming_sortsBy_closestReleases() = runTest {
        responder.on(UrlResponder.MoviesByShowing("FUTURE")) {
            fileAsBody("data-api-service-films-by-showing-type-FUTURE.json")
        }
        val value = feature.getUpcoming().getOrThrow()
            .map { abs(Date().time - it.screeningFrom.time) }
        value.reduce { acc, l ->
            assert(acc <= l)
            l
        }
    }

    @Test
    fun getShowings_returns_filteredElements() = runTest {
        val bookings = prepareBooking()
        whenever(preference.filterSeen).thenReturn(true)
        whenever(bookingDao.selectAll()).thenReturn(bookings)
        prepareShowingsResponse()
        val movieIds = feature.getShowings(cinema, Date(0)).getOrThrow().keys.map { it.id }
        for (booking in bookings)
            assert(booking.movieId !in movieIds)
    }

    @Test
    fun getCurrent_returns_filteredElements() = runTest {
        val bookings = prepareBooking()
        whenever(preference.filterSeen).thenReturn(true)
        whenever(bookingDao.selectAll()).thenReturn(bookings)
        responder.on(UrlResponder.MoviesByShowing("SHOWING")) {
            fileAsBody("data-api-service-films-by-showing-type-SHOWING.json")
        }
        val movieIds = feature.getCurrent().getOrThrow().map { it.id }
        for (booking in bookings)
            assert(booking.movieId !in movieIds)
    }

    @Test
    fun getUpcoming_returns_filteredElements() = runTest {
        val bookings = prepareBooking()
        whenever(preference.filterSeen).thenReturn(true)
        whenever(bookingDao.selectAll()).thenReturn(bookings)
        responder.on(UrlResponder.MoviesByShowing("FUTURE")) {
            fileAsBody("data-api-service-films-by-showing-type-FUTURE.json")
        }
        val movieIds = feature.getUpcoming().getOrThrow().map { it.id }
        for (booking in bookings)
            assert(booking.movieId !in movieIds)
    }

    @Test
    fun getUpcoming_returns_spotColor() = runTest {
        val expectedColor = 0xFF00FF
        whenever(analyzer.getColors(any())).thenReturn(
            Swatch(
                SwatchColor(expectedColor), SwatchColor(expectedColor), SwatchColor(expectedColor)
            )
        )
        responder.on(UrlResponder.MoviesByShowing("FUTURE")) {
            fileAsBody("data-api-service-films-by-showing-type-FUTURE.json")
        }
        val results = feature.getUpcoming().getOrThrow()
        for (result in results)
            assertEquals(expectedColor, result.spotColor)
    }

    @Test
    fun getCurrent_returns_spotColor() = runTest {
        val expectedColor = 0xFF00FF
        whenever(analyzer.getColors(any())).thenReturn(
            Swatch(
                SwatchColor(expectedColor), SwatchColor(expectedColor), SwatchColor(expectedColor)
            )
        )
        responder.on(UrlResponder.MoviesByShowing("SHOWING")) {
            fileAsBody("data-api-service-films-by-showing-type-SHOWING.json")
        }
        val results = feature.getCurrent().getOrThrow()
        for (result in results)
            assertEquals(expectedColor, result.spotColor)
    }

    @Test
    fun getDetail_return_spotColor() = runTest {
        val expectedColor = 0xFF00FF
        whenever(analyzer.getColors(any())).thenReturn(
            Swatch(
                SwatchColor(expectedColor), SwatchColor(expectedColor), SwatchColor(expectedColor)
            )
        )
        val id = "5376O2R"
        responder.on(UrlResponder.Detail(id)) {
            fileAsBody("data-api-service-films-byDistributorCode.json")
        }
        val result = feature.getDetail(MovieFromId(id)).getOrThrow()
        assertEquals(expectedColor, result.spotColor)
    }

    // ---

    private fun prepareShowingsResponse(id: String = cinema.id, date: String = "1970-01-01") {
        responder.on(UrlResponder.EventOccurrence(id, date)) {
            method = HttpMethod.Get
            code = HttpStatusCode.OK
            fileAsBody("quickbook-film-events-in-cinema-at-date.json")
        }
    }

    private fun prepareBooking() = listOf(
        BookingStored(
            id = "",
            name = "",
            startsAt = Date(0),
            paidAt = Date(0),
            movieId = "5145s2r",
            eventId = "",
            cinemaId = cinema.id,
            hall = null
        )
    )

}