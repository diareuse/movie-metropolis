package movie.core.nwk.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
import movie.core.nwk.CinemaService
import movie.core.nwk.CinemaServiceImpl
import movie.core.nwk.CinemaServicePerformance
import movie.core.nwk.EndpointProvider
import movie.core.nwk.EventService
import movie.core.nwk.EventServiceImpl
import movie.core.nwk.EventServicePerformance
import movie.core.nwk.PerformanceTracer
import movie.core.nwk.UserService
import movie.core.nwk.UserServiceImpl
import movie.core.nwk.UserServiceLogout
import movie.core.nwk.UserServicePerformance
import movie.core.nwk.UserServiceReauthorize
import movie.core.nwk.UserServiceSaving

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun serializer(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @ClientRoot
    @Provides
    fun clientRoot(
        engine: HttpClientEngine,
        provider: EndpointProvider,
        serializer: Json = serializer()
    ): HttpClient = HttpClient(engine) {
        install(ContentNegotiation) {
            json(serializer)
        }
        install(HttpCache)
        defaultRequest {
            url("${provider.domain}/mrest/")
            contentType(ContentType.Application.Json)
        }
    }

    @ClientData
    @Provides
    fun clientData(
        @ClientRoot
        client: HttpClient,
        provider: EndpointProvider
    ): HttpClient = client.config {
        defaultRequest {
            url("${provider.domain}/${provider.tld}/data-api-service/v1/${provider.id}/")
        }
    }

    @ClientQuickbook
    @Provides
    fun clientQuickbook(
        @ClientRoot
        client: HttpClient,
        provider: EndpointProvider
    ): HttpClient = client.config {
        defaultRequest {
            url("${provider.domain}/${provider.tld}/data-api-service/v1/quickbook/${provider.id}/")
        }
    }

    @ClientCustomer
    @Provides
    fun clientCustomer(
        @ClientRoot
        client: HttpClient,
        provider: EndpointProvider
    ): HttpClient = client.config {
        defaultRequest {
            url("${provider.domain}/${provider.tld}/group-customer-service/")
        }
    }

    @Provides
    fun engine(): HttpClientEngine = CIO.create()

    @Provides
    fun event(
        @ClientData
        client: HttpClient,
        @ClientQuickbook
        clientQuickbook: HttpClient,
        tracer: PerformanceTracer
    ): EventService {
        var service: EventService
        service = EventServiceImpl(client, clientQuickbook)
        service = EventServicePerformance(service, tracer)
        return service
    }

    @Provides
    fun cinema(
        @ClientRoot
        client: HttpClient,
        tracer: PerformanceTracer
    ): CinemaService {
        var service: CinemaService
        service = CinemaServiceImpl(client)
        service = CinemaServicePerformance(service, tracer)
        return service
    }

    @Provides
    fun user(
        @ClientCustomer
        client: HttpClient,
        account: UserAccount,
        auth: AuthMetadata,
        tracer: PerformanceTracer
    ): UserService {
        var service: UserService
        service = UserServiceImpl(client, account, auth.user, auth.password, auth.captcha)
        service = UserServiceSaving(service, account)
        service = UserServiceReauthorize(service, account, auth.captcha)
        service = UserServiceLogout(service, account)
        service = UserServicePerformance(service, tracer)
        return service
    }

}