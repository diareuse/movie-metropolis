package movie.metropolis.app.screen.booking

import kotlinx.coroutines.test.runTest
import movie.metropolis.app.feature.global.SignInMethod
import movie.metropolis.app.feature.global.di.FacadeModule
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.screen.FeatureTest
import movie.metropolis.app.screen.UrlResponder
import movie.metropolis.app.screen.getOrThrow
import org.junit.Test
import kotlin.test.assertIs

class BookingFacadeTest : FeatureTest() {

    private lateinit var facade: BookingFacade

    override fun prepare() {
        facade = FacadeModule().booking(user)
    }

    @Test
    fun returns_listFromResponse() = runTest {
        prepareLoggedInUser()
        prepareValidResponse()
        val result = facade.getBookings()
        assert(result.isSuccess) { result }
        assert(result.getOrThrow().isNotEmpty())
    }

    @Test
    fun returns_singleActive() = runTest {
        prepareLoggedInUser()
        prepareValidResponse()
        val result = facade.getBookings()
        assert(result.isSuccess) { result }
        assert(result.getOrThrow().count { it is BookingView.Active } == 1) { result }
    }

    @Test
    fun returns_multipleExpired() = runTest {
        prepareLoggedInUser()
        prepareValidResponse()
        val result = facade.getBookings()
        assert(result.isSuccess) { result }
        assert(result.getOrThrow().count { it is BookingView.Expired } == 1) { result }
    }

    @Test
    fun returns_failureWhenSignedOff() = runTest {
        prepareValidResponse()
        val result = facade.getBookings()
        assert(result.isFailure) { result }
        assertIs<SecurityException>(result.exceptionOrNull())
    }

    @Test
    fun returns_failure() = runTest {
        val result = facade.getBookings()
        assert(result.isFailure) { result }
    }

    @Test
    fun printsResults() = runTest {
        prepareLoggedInUser()
        prepareValidResponse()
        println(facade.getBookings().getOrThrow())
    }

    // ---

    private suspend fun prepareLoggedInUser() {
        responder.onUrlRespond(UrlResponder.Auth, "group-customer-service-oauth-token.json")
        user.signIn(SignInMethod.Login("foo", "bar"))
    }

    private fun prepareValidResponse() {
        responder.onUrlRespond(UrlResponder.Cinema, "cinemas.json")
        responder.onUrlRespond(UrlResponder.Booking, "group-customer-service-bookings.json")
        responder.onUrlRespond(
            UrlResponder.Detail(),
            "data-api-service-films-byDistributorCode.json"
        )
        responder.onUrlRespond(UrlResponder.Booking(), "group-customer-service-bookings-id.json")
    }

}