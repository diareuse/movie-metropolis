@file:OptIn(ExperimentalCoroutinesApi::class)

package movie.core

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import movie.core.auth.UserAccount
import movie.core.di.UserFeatureModule
import movie.core.model.SignInMethod
import movie.core.nwk.UserService
import movie.core.nwk.model.RegistrationRequest
import movie.core.nwk.model.TokenRequest
import movie.core.util.wheneverBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.random.Random.Default.nextInt
import kotlin.test.assertEquals

class UserCredentialFeatureTest {

    private lateinit var account: UserAccount
    private lateinit var service: UserService
    private lateinit var feature: UserCredentialFeature

    @Before
    fun prepare() {
        service = mock {}
        account = mock {}
        feature = UserFeatureModule().credential(service, account)
    }

    @Test
    fun email_returns_value() {
        val email = account_responds()
        val result = feature.email
        assertEquals(email, result)
    }

    @Test
    fun signIn_withLogin_passesValues() = runTest {
        feature.signIn(SignInMethod.Login("email", "password"))
        verify(service).getToken(TokenRequest.Login("email", "password"))
    }

    @Test
    fun signIn_withRegistration_passesValues() = runTest {
        feature.signIn(SignInMethod.Registration("email", "password", "first", "last", "phone"))
        verify(service).register(
            RegistrationRequest(
                "email",
                "first",
                "last",
                password = "password",
                phone = "phone"
            )
        )
    }

    @Test
    fun getToken_returns_token() = runTest {
        val token = service_responds_token()
        val actual = feature.getToken().getOrThrow()
        assertEquals(token, actual)
    }

    // ---

    private fun service_responds_token(): String {
        val token = "token${nextInt()}"
        wheneverBlocking { service.getCurrentToken() }.thenReturn(Result.success(token))
        return token
    }

    private fun account_responds(): String {
        val value = "em@il.co.uk"
        whenever(account.email).thenReturn(value)
        return value
    }

}