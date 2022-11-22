package movie.metropolis.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

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

}