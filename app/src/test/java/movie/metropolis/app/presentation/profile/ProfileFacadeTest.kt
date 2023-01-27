@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalCoroutinesApi::class)

package movie.metropolis.app.presentation.profile

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import movie.core.model.Cinema
import movie.core.model.User
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.model.CinemaSimpleView
import movie.metropolis.app.model.MembershipView
import movie.metropolis.app.model.UserView
import movie.metropolis.app.presentation.FeatureTest
import movie.metropolis.app.util.callback
import movie.metropolis.app.util.nextString
import movie.metropolis.app.util.thenBlocking
import movie.metropolis.app.util.wheneverBlocking
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import kotlin.test.assertFails

class ProfileFacadeTest : FeatureTest() {

    private lateinit var facade: ProfileFacade

    override fun prepare() {
        facade = FacadeModule().profile(cinema, data, credentials)
    }

    @Test
    fun returns_cinemas_success() = runTest {
        cinema_responds_success()
        for (result in facade.getCinemas())
            result.getOrThrow()
    }

    @Test
    fun returns_cinemas_failure() = runTest {
        cinema_responds_failure()
        for (result in facade.getCinemas())
            assertFails { result.getOrThrow() }
    }

    @Test
    fun returns_membership_successNotNull() = runTest {
        data_responds_success()
        for (result in facade.getMembership())
            result.getOrThrow()
    }

    @Test
    fun returns_membership_successNull() = runTest {
        data_responds_success()
        for (result in facade.getMembership())
            result.getOrThrow()
    }

    @Test
    fun returns_membership_failure() = runTest {
        data_responds_failure()
        for (result in facade.getMembership())
            assertFails { result.getOrThrow() }
    }

    @Test
    fun returns_user_success() = runTest {
        data_responds_success()
        for (result in facade.getUser())
            result.getOrThrow()
    }

    @Test
    fun returns_user_failure() = runTest {
        data_responds_failure()
        for (result in facade.getUser())
            assertFails { result.getOrThrow() }
    }

    @Test
    fun returns_loggedIn_trueWithSuccess() = runTest {
        credential_responds_success("")
        val result = facade.isLoggedIn()
        assert(result)
    }

    @Test
    fun returns_loggedIn_falseWithFailure() = runTest {
        credential_responds_failure()
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
        data_responds_success(remoteUser)
        facade.save(TestUser())
        verify(data).update(any())
    }

    @Test
    fun savePassword_returns_success() = runTest {
        facade.save(nextString(), nextString())
        verify(data).update(any())
    }

    // ---

    private suspend fun ProfileFacade.getUser(): List<Result<UserView>> {
        val outputs = mutableListOf<Result<UserView>>()
        getUser { outputs += it }
        return outputs
    }

    private suspend fun ProfileFacade.getCinemas(): List<Result<Iterable<CinemaSimpleView>>> {
        val outputs = mutableListOf<Result<Iterable<CinemaSimpleView>>>()
        getCinemas { outputs += it }
        return outputs
    }

    private suspend fun ProfileFacade.getMembership(): List<Result<MembershipView?>> {
        val outputs = mutableListOf<Result<MembershipView?>>()
        getMembership { outputs += it }
        return outputs
    }

    private fun credential_responds_success(value: String): String {
        wheneverBlocking { credentials.getToken() }.thenReturn(Result.success(value))
        return value
    }

    private fun credential_responds_failure() {
        wheneverBlocking { credentials.getToken() }.thenReturn(Result.failure(RuntimeException()))
    }

    private fun data_responds_success(user: User = mock()) {
        wheneverBlocking { data.get(any()) }.thenBlocking {
            callback<User>(0) {
                Result.success(user)
            }
        }
    }

    private fun data_responds_failure() {
        wheneverBlocking { data.get(any()) }.thenBlocking {
            callback<User>(0) {
                Result.failure(RuntimeException())
            }
        }
    }

    private fun cinema_responds_success() {
        wheneverBlocking { cinema.get(anyOrNull(), any()) }.thenBlocking {
            callback<Iterable<Cinema>>(1) {
                Result.success(listOf(mock()))
            }
        }
    }

    private fun cinema_responds_failure() {
        wheneverBlocking { cinema.get(anyOrNull(), any()) }.thenBlocking {
            callback<Iterable<Cinema>>(1) {
                Result.failure(RuntimeException())
            }
        }
    }

    // ---

    private data class TestUser(
        override val firstName: String = nextString(),
        override val lastName: String = nextString(),
        override val email: String = nextString(),
        override val phone: String = nextString(),
        override val favorite: CinemaSimpleView? = TestCinema(),
        override val consent: UserView.ConsentView = Consent()
    ) : UserView

    private data class TestCinema(
        override val id: String = nextString(),
        override val name: String = nextString(),
        override val city: String = nextString()
    ) : CinemaSimpleView

    private data class Consent(
        override val marketing: Boolean = true,
        override val premium: Boolean = true
    ) : UserView.ConsentView

}