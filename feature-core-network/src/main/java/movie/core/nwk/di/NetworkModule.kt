package movie.core.nwk.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.plugin
import io.ktor.http.ContentType
import io.ktor.http.contentLength
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import movie.core.auth.AuthMetadata
import movie.core.auth.UserAccount
import movie.core.nwk.CinemaService
import movie.core.nwk.CinemaServiceImpl
import movie.core.nwk.EndpointProvider
import movie.core.nwk.EventService
import movie.core.nwk.EventServiceImpl
import movie.core.nwk.LazyHttpClient
import movie.core.nwk.LazyHttpClientEngine
import movie.core.nwk.PerformanceTracer
import movie.core.nwk.UserService
import movie.core.nwk.UserServiceImpl
import movie.core.nwk.UserServiceLogout
import movie.core.nwk.UserServiceReauthorize
import movie.core.nwk.UserServiceSaving
import movie.core.nwk.trace
import javax.inject.Singleton

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
        tracer: PerformanceTracer,
        serializer: Json = serializer()
    ): LazyHttpClient = LazyHttpClient {
        HttpClient(engine) {
            install(ContentNegotiation) {
                json(serializer)
            }
            install(HttpCache)
            defaultRequest {
                url("${provider.domain}/mrest/")
                contentType(ContentType.Application.Json)
            }
        }.apply {
            plugin(HttpSend).intercept { request ->
                tracer.trace(request.url.buildString(), request.method.value) { trace ->
                    trace.setRequestLength(request.contentLength() ?: 0)
                    execute(request).also { call ->
                        trace.setResponseCode(call.response.status.value)
                        trace.setResponseLength(call.response.contentLength() ?: 0)
                        trace.setAttribute("version", call.response.version.toString())
                    }
                }
            }
        }
    }

    @ClientData
    @Provides
    fun clientData(
        @ClientRoot
        client: LazyHttpClient,
        provider: EndpointProvider
    ): LazyHttpClient = client.modify {
        config {
            defaultRequest {
                url("${provider.domain}/${provider.tld}/data-api-service/v1/${provider.id}/")
            }
        }
    }

    @ClientQuickbook
    @Provides
    fun clientQuickbook(
        @ClientRoot
        client: LazyHttpClient,
        provider: EndpointProvider
    ): LazyHttpClient = client.modify {
        config {
            defaultRequest {
                url("${provider.domain}/${provider.tld}/data-api-service/v1/quickbook/${provider.id}/")
            }
        }
    }

    @ClientCustomer
    @Provides
    fun clientCustomer(
        @ClientRoot
        client: LazyHttpClient,
        provider: EndpointProvider
    ): LazyHttpClient = client.modify {
        config {
            defaultRequest {
                url("${provider.domain}/${provider.tld}/group-customer-service/")
            }
        }
    }

    @Singleton
    @Provides
    fun engine(): HttpClientEngine = LazyHttpClientEngine { CIO.create() }

    @Provides
    fun event(
        @ClientData
        client: LazyHttpClient,
        @ClientQuickbook
        clientQuickbook: LazyHttpClient
    ): EventService {
        val service: EventService
        service = EventServiceImpl(client, clientQuickbook)
        return service
    }

    @Provides
    fun cinema(
        @ClientRoot
        client: LazyHttpClient
    ): CinemaService {
        val service: CinemaService
        service = CinemaServiceImpl(client)
        return service
    }

    @Provides
    fun user(
        @ClientCustomer
        client: LazyHttpClient,
        account: UserAccount,
        auth: AuthMetadata
    ): UserService {
        var service: UserService
        service = UserServiceImpl(client, account, auth.user, auth.password, auth.captcha)
        service = UserServiceSaving(service, account)
        service = UserServiceReauthorize(service, account, auth.captcha)
        service = UserServiceLogout(service, account)
        return service
    }

}