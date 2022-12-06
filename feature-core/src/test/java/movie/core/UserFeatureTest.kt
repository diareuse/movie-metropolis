package movie.core

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import movie.core.auth.AuthMetadata
import movie.core.auth.UserAccount
import movie.core.auth.UserCredentials
import movie.core.di.EventFeatureModule
import movie.core.di.UserFeatureModule
import movie.core.model.FieldUpdate
import movie.core.model.SignInMethod
import movie.core.nwk.di.NetworkModule
import org.junit.Test
import org.mockito.kotlin.spy
import java.util.Date
import kotlin.test.assertFails

class UserFeatureTest : FeatureTest() {
    private lateinit var account: UserAccount
    private lateinit var credentials: UserCredentials
    private lateinit var feature: UserFeature

    override fun prepare() {
        account = spy(MockAccount())
        credentials = spy(MockCredentials())
        val network = NetworkModule()
        val auth = AuthMetadata("user", "password", "captcha")
        val service = network.user(clientCustomer, account, credentials, auth)
        val event = EventFeatureModule().feature(
            event = network.event(clientData),
            cinema = network.cinema(clientRoot)
        )
        feature = UserFeatureModule().feature(service, event)
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
        feature.getBookings().getOrThrow()
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
            get() = token != null
        override var token: String? = null
        override var refreshToken: String? = null
        override var expirationDate: Date? = null
        override fun delete() {}
    }

    private open class MockCredentials : UserCredentials {
        override var email: String? = "test@google.com"
        override var password: String? = "foobartoolbar"
    }

}

