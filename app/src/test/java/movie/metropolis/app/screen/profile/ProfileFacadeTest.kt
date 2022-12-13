package movie.metropolis.app.screen.profile

import kotlinx.coroutines.test.runTest
import movie.core.model.User
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.model.CinemaSimpleView
import movie.metropolis.app.model.UserView
import movie.metropolis.app.screen.FeatureTest
import movie.metropolis.app.util.nextString
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ProfileFacadeTest : FeatureTest() {

    private lateinit var facade: ProfileFacade

    override fun prepare() {
        facade = FacadeModule().profile(event, user)
    }

    @Test
    fun returns_cinemas_success() = runTest {
        whenever(event.getCinemas(null)).thenReturn(Result.success(listOf(mock())))
        val result = facade.getCinemas()
        assert(result.isSuccess) { result }
    }

    @Test
    fun returns_cinemas_failure() = runTest {
        whenever(event.getCinemas(null)).thenReturn(Result.failure(RuntimeException()))
        val result = facade.getCinemas()
        assert(result.isFailure) { result }
    }

    @Test
    fun returns_membership_successNotNull() = runTest {
        whenever(user.getUser()).thenReturn(Result.success(mock()))
        val result = facade.getMembership()
        assert(result.isSuccess) { result }
    }

    @Test
    fun returns_membership_successNull() = runTest {
        whenever(user.getUser()).thenReturn(Result.success(mock()))
        val result = facade.getMembership()
        assert(result.isSuccess) { result }
    }

    @Test
    fun returns_membership_failure() = runTest {
        whenever(user.getUser()).thenReturn(Result.failure(RuntimeException()))
        val result = facade.getMembership()
        assert(result.isFailure) { result }
    }

    @Test
    fun returns_user_success() = runTest {
        whenever(user.getUser()).thenReturn(Result.success(mock()))
        val result = facade.getUser()
        assert(result.isSuccess) { result }
    }

    @Test
    fun returns_user_failure() = runTest {
        whenever(user.getUser()).thenReturn(Result.failure(RuntimeException()))
        val result = facade.getUser()
        assert(result.isFailure) { result }
    }

    @Test
    fun returns_loggedIn_trueWithSuccess() = runTest {
        whenever(user.getToken()).thenReturn(Result.success(""))
        val result = facade.isLoggedIn()
        assert(result)
    }

    @Test
    fun returns_loggedIn_falseWithFailure() = runTest {
        whenever(user.getToken()).thenReturn(Result.failure(RuntimeException()))
        val result = facade.isLoggedIn()
        assert(!result)
    }

    @Test
    fun save_returns_success() = runTest {
        val remoteConsent = mock<User.Consent> {
            on { marketing }.thenReturn(false)
            on { premium }.thenReturn(false)
        }
        val remoteUser = mock<User> {
            on { firstName }.thenReturn("")
            on { lastName }.thenReturn("")
            on { email }.thenReturn("")
            on { phone }.thenReturn("")
            on { favorite }.thenReturn(null)
            on { consent }.thenReturn(remoteConsent)
        }
        whenever(user.getUser()).thenReturn(Result.success(remoteUser))
        whenever(user.update(any())).thenReturn(Result.success(mock()))
        val result = facade.save(TestUser())
        assert(result.isSuccess) { result }
    }

    @Test
    fun save_returns_failure() = runTest {
        val remoteConsent = mock<User.Consent> {
            on { marketing }.thenReturn(false)
            on { premium }.thenReturn(false)
        }
        val remoteUser = mock<User> {
            on { firstName }.thenReturn("")
            on { lastName }.thenReturn("")
            on { email }.thenReturn("")
            on { phone }.thenReturn("")
            on { favorite }.thenReturn(null)
            on { consent }.thenReturn(remoteConsent)
        }
        whenever(user.getUser()).thenReturn(Result.success(remoteUser))
        whenever(user.update(any())).thenReturn(Result.failure(RuntimeException()))
        val result = facade.save(TestUser())
        assert(result.isFailure) { result }
    }

    @Test
    fun savePassword_returns_success() = runTest {
        whenever(user.update(any())).thenReturn(Result.success(mock()))
        val result = facade.save(nextString(), nextString())
        assert(result.isSuccess) { result }
    }

    @Test
    fun savePassword_returns_failure() = runTest {
        whenever(user.update(any())).thenReturn(Result.failure(RuntimeException()))
        val result = facade.save(nextString(), nextString())
        assert(result.isFailure) { result }
    }

    // ---

    private data class TestUser(
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