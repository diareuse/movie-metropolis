package movie.metropolis.app.screen.profile

import io.ktor.http.HttpMethod
import kotlinx.coroutines.test.runTest
import movie.core.model.SignInMethod
import movie.metropolis.app.feature.global.di.FacadeModule
import movie.metropolis.app.model.CinemaSimpleView
import movie.metropolis.app.model.UserView
import movie.metropolis.app.screen.FeatureTest
import movie.metropolis.app.screen.UrlResponder
import movie.metropolis.app.util.nextString
import org.junit.Test

class ProfileFacadeTest : FeatureTest() {

    private lateinit var facade: ProfileFacade

    override fun prepare() {
        facade = FacadeModule().profile(event, user)
    }

    @Test
    fun returns_cinemas_success() = runTest {
        responder.onUrlRespond(UrlResponder.Cinema, "cinemas.json")
        val result = facade.getCinemas()
        assert(result.isSuccess) { result }
    }

    @Test
    fun returns_cinemas_failure() = runTest {
        val result = facade.getCinemas()
        assert(result.isFailure) { result }
    }

    @Test
    fun returns_membership_successNotNull() = runTest {
        prepareLoggedInUser()
        responder.onUrlRespond(
            UrlResponder.Customer,
            "group-customer-service-customers-current.json"
        )
        responder.onUrlRespond(UrlResponder.Cinema, "cinemas.json")
        responder.onUrlRespond(
            UrlResponder.CustomerPoints,
            "group-customer-service-customer-points.json"
        )
        val result = facade.getMembership()
        assert(result.isSuccess) { result }
    }

    @Test
    fun returns_membership_successNull() = runTest {
        prepareLoggedInUser()
        responder.onUrlRespond(
            UrlResponder.Customer,
            "group-customer-service-customers-current-nomember.json"
        )
        responder.onUrlRespond(UrlResponder.Cinema, "cinemas.json")
        responder.onUrlRespond(
            UrlResponder.CustomerPoints,
            "group-customer-service-customer-points.json"
        )
        val result = facade.getMembership()
        assert(result.isSuccess) { result }
    }

    @Test
    fun returns_membership_failure() = runTest {
        prepareLoggedInUser()
        val result = facade.getMembership()
        assert(result.isFailure) { result }
    }

    @Test
    fun returns_user_success() = runTest {
        prepareLoggedInUser()
        responder.onUrlRespond(
            UrlResponder.Customer,
            "group-customer-service-customers-current.json"
        )
        responder.onUrlRespond(UrlResponder.Cinema, "cinemas.json")
        responder.onUrlRespond(
            UrlResponder.CustomerPoints,
            "group-customer-service-customer-points.json"
        )
        val result = facade.getUser()
        assert(result.isSuccess) { result }
    }

    @Test
    fun returns_user_failure() = runTest {
        prepareLoggedInUser()
        val result = facade.getUser()
        assert(result.isFailure) { result }
    }

    @Test
    fun returns_loggedIn_trueWithSuccess() = runTest {
        prepareLoggedInUser()
        val result = facade.isLoggedIn()
        assert(result)
    }

    @Test
    fun returns_loggedIn_falseWithSuccess() = runTest {
        val result = facade.isLoggedIn()
        assert(!result)
    }

    @Test
    fun returns_loggedIn_falseWithFailure() = runTest {
        val result = facade.isLoggedIn()
        assert(!result)
    }

    @Test
    fun save_returns_success() = runTest {
        prepareLoggedInUser()
        responder.onUrlRespond(
            UrlResponder.Customer,
            "group-customer-service-customers-current.json"
        )
        responder.onUrlRespond(UrlResponder.Cinema, "cinemas.json")
        responder.onUrlRespond(
            UrlResponder.CustomerPoints,
            "group-customer-service-customer-points.json"
        )
        responder.onUrlRespond(
            HttpMethod.Put,
            UrlResponder.Customer,
            "group-customer-service-customers.json"
        )
        val result = facade.save(User())
        assert(result.isSuccess) { result }
    }

    @Test
    fun save_returns_failure() = runTest {
        prepareLoggedInUser()
        val result = facade.save(User())
        assert(result.isFailure) { result }
    }

    @Test
    fun savePassword_returns_success() = runTest {
        prepareLoggedInUser()
        responder.onUrlRespond(
            UrlResponder.Customer,
            "group-customer-service-customers-current.json"
        )
        responder.onUrlRespond(UrlResponder.Cinema, "cinemas.json")
        responder.onUrlRespond(
            UrlResponder.CustomerPoints,
            "group-customer-service-customer-points.json"
        )
        responder.onUrlRespond(
            HttpMethod.Put,
            UrlResponder.Customer,
            "group-customer-service-customers.json"
        )
        responder.onUrlRespond(HttpMethod.Put, UrlResponder.Password, "")
        val result = facade.save(nextString(), nextString())
        assert(result.isSuccess) { result }
    }

    @Test
    fun savePassword_returns_failure() = runTest {
        prepareLoggedInUser()
        val result = facade.save(nextString(), nextString())
        assert(result.isFailure) { result }
    }

    // ---

    private suspend fun prepareLoggedInUser() {
        responder.onUrlRespond(UrlResponder.Auth, "group-customer-service-oauth-token.json")
        user.signIn(SignInMethod.Login("foo", "bar"))
    }

    private data class User(
        override val firstName: String = nextString(),
        override val lastName: String = nextString(),
        override val email: String = nextString(),
        override val phone: String = nextString(),
        override val favorite: CinemaSimpleView? = Cinema(),
        override val consent: UserView.ConsentView = Consent()
    ) : UserView

    private data class Cinema(
        override val id: String = nextString(),
        override val name: String = nextString(),
        override val city: String = nextString()
    ) : CinemaSimpleView

    private data class Consent(
        override val marketing: Boolean = true,
        override val premium: Boolean = true
    ) : UserView.ConsentView

}