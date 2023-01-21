@file:OptIn(ExperimentalCoroutinesApi::class)

package movie.core

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import movie.calendar.CalendarWriter
import movie.core.auth.AuthMetadata
import movie.core.auth.UserAccount
import movie.core.db.dao.BookingDao
import movie.core.db.dao.BookingSeatsDao
import movie.core.db.dao.CinemaDao
import movie.core.db.dao.MovieDetailDao
import movie.core.db.dao.MovieMediaDao
import movie.core.di.UserFeatureModule
import movie.core.model.Cinema
import movie.core.model.FieldUpdate
import movie.core.model.MovieDetail
import movie.core.model.SignInMethod
import movie.core.nwk.di.NetworkModule
import movie.core.preference.EventPreference
import movie.core.util.callback
import movie.core.util.thenAnswerSus
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.Date
import kotlin.test.assertFails

class UserFeatureTest : FeatureTest() {

    private lateinit var movie: EventDetailFeature
    private lateinit var cinema: EventCinemaFeature
    private lateinit var store: TicketStore
    private lateinit var writer: CalendarWriter
    private lateinit var preference: EventPreference
    private lateinit var account: UserAccount
    private lateinit var feature: UserFeature
    private lateinit var bookingDao: BookingDao
    private lateinit var seatsDao: BookingSeatsDao
    private lateinit var detailDao: MovieDetailDao
    private lateinit var cinemaDao: CinemaDao
    private lateinit var mediaDao: MovieMediaDao

    override fun prepare() {
        bookingDao = mock()
        seatsDao = mock()
        detailDao = mock()
        cinemaDao = mock()
        mediaDao = mock()
        account = spy(MockAccount())
        preference = mock()
        writer = mock {
            on { write(any()) }.then { }
        }
        cinema = mock {
            onBlocking { get(anyOrNull(), any()) }.thenAnswerSus {
                callback<Iterable<Cinema>>(1) {
                    Result.success(listOf(mock()))
                }
            }
        }
        movie = mock {
            onBlocking { get(any(), any()) }.thenAnswerSus {
                callback<MovieDetail>(1) {
                    Result.success(mock())
                }
            }
        }
        store = TicketStore()
        whenever(preference.filterSeen).thenReturn(false)
        whenever(preference.calendarId).thenReturn("")
        val network = NetworkModule()
        val auth = AuthMetadata("user", "password", "captcha")
        val service = network.user(clientCustomer, account, auth)
        feature = UserFeatureModule().saving(
            service = service,
            cinema = cinema,
            movie = movie,
            bookingDao = bookingDao,
            seatsDao = seatsDao,
            account = account,
            writer = { writer },
            preference = preference,
            store = store
        )
        feature = UserFeatureModule().feature(
            account = account,
            bookingDao = bookingDao,
            seatsDao = seatsDao,
            movieDao = detailDao,
            cinemaDao = cinemaDao,
            mediaDao = mediaDao,
            saving = feature
        )
    }

    // ---

    @Test
    fun signIn_responds_withSuccess() = runTest {
        responder.on(UrlResponder.Auth) {
            method = HttpMethod.Post
            code = HttpStatusCode.Accepted
            fileAsBody("group-customer-service-oauth-token.json")
        }
        feature.signIn(SignInMethod.Login("test@google.com", "foobartoolbar")).getOrThrow()
    }

    @Test
    fun signIn_responds_withFailure() = runTest {
        responder.on(UrlResponder.Auth) {
            method = HttpMethod.Post
            code = HttpStatusCode.BadRequest
            body = ""
        }
        assertFails {
            feature.signIn(SignInMethod.Login("test@google.com", "foobartoolbar")).getOrThrow()
        }
    }

    @Test
    fun signInRegister_responds_withSuccess() = runTest {
        responder.on(UrlResponder.Register) {
            method = HttpMethod.Post
            code = HttpStatusCode.OK
            fileAsBody("group-customer-service-customers.json")
        }
        feature.signIn(SignInMethod.Registration("", "", "", "", "")).getOrThrow()
    }

    @Test
    fun signInRegister_responds_withFailure() = runTest {
        responder.on(UrlResponder.Register) {
            method = HttpMethod.Post
            code = HttpStatusCode.BadRequest
            body = ""
        }
        assertFails {
            feature.signIn(SignInMethod.Registration("", "", "", "", "")).getOrThrow()
        }
    }

    @Test
    fun updateCinema_responds_withSuccess() = runTest {
        prepareLoggedInUser()
        prepareCustomerUpdateResponse()
        feature.update(listOf(FieldUpdate.Cinema("id"))).getOrThrow()
    }

    @Test
    fun updateConsentMarketing_responds_withSuccess() = runTest {
        prepareLoggedInUser()
        prepareCustomerUpdateResponse()
        feature.update(listOf(FieldUpdate.Consent.Marketing(true))).getOrThrow()
    }

    @Test
    fun updateConsentPremium_responds_withSuccess() = runTest {
        prepareLoggedInUser()
        prepareCustomerUpdateResponse()
        feature.update(listOf(FieldUpdate.Consent.Premium(true))).getOrThrow()
    }

    @Test
    fun updateEmail_responds_withSuccess() = runTest {
        prepareLoggedInUser()
        prepareCustomerUpdateResponse()
        feature.update(listOf(FieldUpdate.Email("email"))).getOrThrow()
    }

    @Test
    fun updateNameFirst_responds_withSuccess() = runTest {
        prepareLoggedInUser()
        prepareCustomerUpdateResponse()
        feature.update(listOf(FieldUpdate.Name.First("first"))).getOrThrow()
    }

    @Test
    fun updateNameLast_responds_withSuccess() = runTest {
        prepareLoggedInUser()
        prepareCustomerUpdateResponse()
        feature.update(listOf(FieldUpdate.Name.Last("last"))).getOrThrow()
    }

    @Test
    fun updatePhone_responds_withSuccess() = runTest {
        prepareLoggedInUser()
        prepareCustomerUpdateResponse()
        feature.update(listOf(FieldUpdate.Phone("+1123456789"))).getOrThrow()
    }

    @Test
    fun updatePassword_responds_withSuccess() = runTest {
        prepareLoggedInUser()
        prepareCustomerUpdateResponse()
        responder.on(UrlResponder.Password) {
            method = HttpMethod.Put
            code = HttpStatusCode.OK
            body = ""
        }
        feature.update(listOf(FieldUpdate.Password("a", "b"))).getOrThrow()
    }

    @Test
    fun updateUserData_responds_withFailure() = runTest {
        prepareLoggedInUser()
        prepareCustomerUpdateResponse()
        responder.on(UrlResponder.Customer) {
            method = HttpMethod.Put
            code = HttpStatusCode.BadRequest
            body = ""
        }
        assertFails {
            feature.update(listOf(FieldUpdate.Phone("+1123456789"))).getOrThrow()
        }
    }

    @Test
    fun updatePassword_responds_withFailure() = runTest {
        prepareLoggedInUser()
        prepareCustomerUpdateResponse()
        responder.on(UrlResponder.Password) {
            method = HttpMethod.Put
            code = HttpStatusCode.BadRequest
            body = ""
        }
        assertFails {
            feature.update(listOf(FieldUpdate.Password("a", "b"))).getOrThrow()
        }
    }

    @Test
    fun getUser_responds_withSuccess() = runTest {
        prepareLoggedInUser()
        prepareCustomerResponse()
        feature.getUser().getOrThrow()
    }

    @Test
    fun getUser_responds_withFailure() = runTest {
        prepareLoggedInUser()
        prepareCustomerResponse()
        responder.on(UrlResponder.Customer) {
            method = HttpMethod.Get
            code = HttpStatusCode.BadRequest
            body = ""
        }
        assertFails {
            feature.getUser().getOrThrow()
        }
    }

    @Test
    fun getBookings_responds_withSuccess() = runTest {
        val model = mock<Cinema> {
            on { id }.thenReturn("1051")
        }
        whenever(cinema.get(anyOrNull(), any())).thenAnswerSus {
            callback<Iterable<Cinema>>(1) {
                Result.success(listOf(model))
            }
        }
        whenever(movie.get(any(), any())).thenAnswerSus {
            callback<MovieDetail>(1) {
                Result.failure(Throwable())
            }
        }
        prepareLoggedInUser()
        responder.on(UrlResponder.Booking) {
            method = HttpMethod.Get
            code = HttpStatusCode.OK
            fileAsBody("group-customer-service-bookings.json")
        }
        responder.on(UrlResponder.Booking()) {
            method = HttpMethod.Get
            code = HttpStatusCode.OK
            fileAsBody("group-customer-service-bookings-id.json")
        }
        val value = feature.getBookings().getOrThrow()
        assert(value.toList().isEmpty()) { "Expected to be empty, but was $value." }
    }

    @Test
    fun getBookings_responds_withSuccess_hasMovie() = runTest {
        val model = mock<Cinema> {
            on { id }.thenReturn("1051")
        }
        val detail = mock<MovieDetail> {
            on { id }.thenReturn("id")
        }
        whenever(cinema.get(anyOrNull(), any())).thenAnswerSus {
            callback<Iterable<Cinema>>(1) {
                Result.success(listOf(model))
            }
        }
        whenever(movie.get(any(), any())).thenAnswerSus {
            callback(1) {
                Result.success(detail)
            }
        }
        prepareLoggedInUser()
        responder.on(UrlResponder.Booking) {
            method = HttpMethod.Get
            code = HttpStatusCode.OK
            fileAsBody("group-customer-service-bookings.json")
        }
        responder.on(UrlResponder.Booking()) {
            method = HttpMethod.Get
            code = HttpStatusCode.OK
            fileAsBody("group-customer-service-bookings-id.json")
        }
        responder.on(UrlResponder.Detail()) {
            method = HttpMethod.Get
            code = HttpStatusCode.OK
            fileAsBody("data-api-service-films-byDistributorCode.json")
        }
        responder.on(UrlResponder.Cinema) {
            method = HttpMethod.Get
            code = HttpStatusCode.OK
            fileAsBody("cinemas.json")
        }
        val value = feature.getBookings().getOrThrow()
        assert(value.toList().isNotEmpty()) { "Expected to contain elements, but was empty." }
    }

    @Test
    fun getBookings_responds_withFailure() = runTest {
        prepareLoggedInUser()
        responder.on(UrlResponder.Booking) {
            method = HttpMethod.Get
            code = HttpStatusCode.BadRequest
            body = ""
        }
        assertFails {
            feature.getBookings().getOrThrow()
        }
    }

    @Test
    fun getToken_responds_withSuccess() = runTest {
        prepareLoggedInUser()
        feature.getToken().getOrThrow()
    }

    @Test
    fun getToken_responds_withFailure() = runTest {
        assertFails {
            feature.getToken().getOrThrow()
        }
    }

    @Test
    fun getBooking_savesEvent() = runTest {
        val cinema = mock<Cinema> {
            on { id }.thenReturn("1051")
        }
        val movie = mock<MovieDetail> {
            on { id }.thenReturn("id")
        }
        prepareLoggedInUser()
        prepareBooking(cinema, movie)
        feature.getBookings().getOrThrow()
        writer.inOrder {
            verify(times(3)).write(any())
        }
    }

    @Test
    fun getBooking_savesSharedTicket() = runTest {
        val cinema = mock<Cinema> {
            on { id }.thenReturn("1051")
        }
        val movie = mock<MovieDetail> {
            on { id }.thenReturn("id")
            on { name }.thenReturn("name")
        }
        store.add(mock {
            on { id }.thenReturn("")
            on { startsAt }.thenReturn(Date())
            on { venue }.thenReturn("")
            on { movieId }.thenReturn("id")
            on { eventId }.thenReturn("")
            on { cinemaId }.thenReturn("1051")
            on { seats }.thenReturn(emptyList())
        })
        prepareLoggedInUser()
        prepareBooking(cinema, movie)
        responder.on(UrlResponder.Booking) {
            method = HttpMethod.Get
            code = HttpStatusCode.OK
            body = "[]"
        }
        feature.getBookings().getOrThrow()
        verify(bookingDao).insertOrUpdate(any())
    }

    @Test
    fun getBooking_savesSharedTicket_withoutLogin() = runTest {
        val cinema = mock<Cinema> {
            on { id }.thenReturn("1051")
        }
        val movie = mock<MovieDetail> {
            on { id }.thenReturn("id")
            on { name }.thenReturn("name")
        }
        store.add(mock {
            on { id }.thenReturn("")
            on { startsAt }.thenReturn(Date())
            on { venue }.thenReturn("")
            on { movieId }.thenReturn("id")
            on { eventId }.thenReturn("")
            on { cinemaId }.thenReturn("1051")
            on { seats }.thenReturn(emptyList())
        })
        prepareBooking(cinema, movie)
        responder.on(UrlResponder.Booking) {
            method = HttpMethod.Get
            code = HttpStatusCode.OK
            body = "[]"
        }
        feature.getBookings().getOrThrow()
        verify(bookingDao).insertOrUpdate(any())
    }

    // ---

    private suspend fun prepareBooking(
        cinema: Cinema,
        movie: MovieDetail
    ) {
        whenever(this.cinema.get(anyOrNull(), any())).thenAnswerSus {
            callback<Iterable<Cinema>>(1) {
                Result.success(listOf(cinema))
            }
        }
        whenever(this.movie.get(any(), any())).thenAnswerSus {
            callback(1) {
                Result.success(movie)
            }
        }
        responder.on(UrlResponder.Booking) {
            method = HttpMethod.Get
            code = HttpStatusCode.OK
            fileAsBody("group-customer-service-bookings.json")
        }
        responder.on(UrlResponder.Booking()) {
            method = HttpMethod.Get
            code = HttpStatusCode.OK
            fileAsBody("group-customer-service-bookings-id.json")
        }
        responder.on(UrlResponder.Detail()) {
            method = HttpMethod.Get
            code = HttpStatusCode.OK
            fileAsBody("data-api-service-films-byDistributorCode.json")
        }
        responder.on(UrlResponder.Cinema) {
            method = HttpMethod.Get
            code = HttpStatusCode.OK
            fileAsBody("cinemas.json")
        }
    }

    private suspend fun prepareLoggedInUser() {
        responder.on(UrlResponder.Auth) {
            method = HttpMethod.Post
            code = HttpStatusCode.OK
            fileAsBody("group-customer-service-oauth-token.json")
        }
        feature.signIn(SignInMethod.Login("foo", "bar"))
    }

    private fun prepareCustomerResponse() {
        responder.on(UrlResponder.Customer) {
            method = HttpMethod.Get
            code = HttpStatusCode.OK
            fileAsBody("group-customer-service-customers-current.json")
        }
        responder.on(UrlResponder.CustomerPoints) {
            method = HttpMethod.Get
            code = HttpStatusCode.OK
            fileAsBody("group-customer-service-customer-points.json")
        }
        responder.on(UrlResponder.Cinema) {
            method = HttpMethod.Get
            code = HttpStatusCode.OK
            fileAsBody("cinemas.json")
        }
    }

    private fun prepareCustomerUpdateResponse() {
        prepareCustomerResponse()
        responder.on(UrlResponder.Customer) {
            method = HttpMethod.Put
            code = HttpStatusCode.OK
            fileAsBody("group-customer-service-customers.json")
        }
    }

    // ---

    private open class MockAccount : UserAccount {
        override var token: String? = null
        override var refreshToken: String? = null
        override var expirationDate: Date? = null
        override var email: String? = null
        override var password: String? = null
        override val isLoggedIn: Boolean
            get() = password != null
    }

}

