package movie.core.nwk.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import movie.core.auth.AuthMetadata
import movie.core.auth.UserAccount
import movie.core.auth.UserCredentials
import movie.core.nwk.CinemaService
import movie.core.nwk.CinemaServiceImpl
import movie.core.nwk.EventService
import movie.core.nwk.EventServiceImpl
import movie.core.nwk.UserService
import movie.core.nwk.UserServiceImpl
import movie.core.nwk.UserServiceLogout
import movie.core.nwk.UserServiceReauthorize
import movie.core.nwk.UserServiceSaving

@Module
@InstallIn(ActivityRetainedComponent::class)
class NetworkModule {

    @ClientRoot
    @Provides
    fun clientRoot(
        engine: HttpClientEngine
    ): HttpClient = HttpClient(engine) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                explicitNulls = false
            })
        }
        install(HttpCache)
        defaultRequest {
            url("https://www.cinemacity.cz/mrest/")
            contentType(ContentType.Application.Json)
        }
    }

    @ClientData
    @Provides
    fun clientData(
        @ClientRoot
        client: HttpClient
    ): HttpClient = client.config {
        defaultRequest {
            url("https://www.cinemacity.cz/cz/data-api-service/v1/")
        }
    }

    @ClientCustomer
    @Provides
    fun clientCustomer(
        @ClientRoot
        client: HttpClient
    ): HttpClient = client.config {
        defaultRequest {
            url("https://www.cinemacity.cz/cz/group-customer-service/")
        }
    }

    @Provides
    fun engine(): HttpClientEngine = CIO.create()

    @Provides
    fun event(
        @ClientData
        client: HttpClient
    ): EventService {
        return EventServiceImpl(client)
    }

    @Provides
    fun cinema(
        @ClientRoot
        client: HttpClient
    ): CinemaService {
        return CinemaServiceImpl(client)
    }

    @Provides
    fun user(
        @ClientCustomer
        client: HttpClient,
        account: UserAccount,
        credentials: UserCredentials,
        auth: AuthMetadata
    ): UserService {
        var service: UserService
        service = UserServiceImpl(client, account, auth.user, auth.password, auth.captcha)
        service = UserServiceSaving(service, credentials, account)
        service = UserServiceReauthorize(service, credentials, account, auth.captcha)
        service = UserServiceLogout(service, credentials, account)
        return service
    }

}