package movie.core

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import movie.core.auth.AuthMetadata
import movie.core.auth.UserAccount
import movie.core.db.dao.BookingDao
import movie.core.db.dao.BookingSeatsDao
import movie.core.db.dao.CinemaDao
import movie.core.db.dao.MovieDao
import movie.core.db.dao.MovieDetailDao
import movie.core.db.dao.MovieMediaDao
import movie.core.db.dao.MoviePreviewDao
import movie.core.db.dao.MovieReferenceDao
import movie.core.db.dao.ShowingDao
import movie.core.di.EventFeatureModule
import movie.core.di.UserFeatureModule
import movie.core.model.FieldUpdate
import movie.core.model.SignInMethod
import movie.core.nwk.di.NetworkModule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import java.util.Date
import kotlin.test.assertFails

class UserFeatureTest : FeatureTest() {

    private lateinit var account: UserAccount
    private lateinit var feature: UserFeature
    private lateinit var bookingDao: BookingDao
    private lateinit var seatsDao: BookingSeatsDao
    private lateinit var detailDao: MovieDetailDao
    private lateinit var cinemaDao: CinemaDao
    private lateinit var mediaDao: MovieMediaDao
    private lateinit var showingDao: ShowingDao
    private lateinit var referenceDao: MovieReferenceDao
    private lateinit var previewDao: MoviePreviewDao
    private lateinit var movieDao: MovieDao

    override fun prepare() {
        bookingDao = mock()
        seatsDao = mock()
        detailDao = mock()
        cinemaDao = mock()
        mediaDao = mock()
        showingDao = mock()
        referenceDao = mock()
        previewDao = mock()
        movieDao = mock()
        account = spy(MockAccount())
        val network = NetworkModule()
        val auth = AuthMetadata("user", "password", "captcha")
        val service = network.user(clientCustomer, account, auth)
        val event = EventFeatureModule().feature(
            event = network.event(clientData),
            cinema = network.cinema(clientRoot),
            showingDao = showingDao,
            cinemaDao = cinemaDao,
            detailDao = detailDao,
            mediaDao = mediaDao,
            referenceDao = referenceDao,
            previewDao = previewDao,
            movieDao = movieDao
        )
        feature = UserFeatureModule().feature(
            service = service,
            event = event,
            account = account,
            bookingDao = bookingDao,
            seatsDao = seatsDao,
            movieDao = detailDao,
            cinemaDao = cinemaDao,
            mediaDao = mediaDao
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

    // ---

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
        override val isLoggedIn: Boolean
            get() = email != null
        override var token: String? = null
        override var refreshToken: String? = null
        override var expirationDate: Date? = null
        override var email: String? = null
        override var password: String? = null
    }

}

