package movie.metropolis.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.defaultRequest
import java.util.Locale

@Module
@InstallIn(ActivityRetainedComponent::class)
class NetworkModule {

    @ClientRoot
    @Provides
    fun clientRoot(
        engine: HttpClientEngine
    ): HttpClient = HttpClient(engine) {
        defaultRequest {
            url("https://www.cinemacity.cz/mrest")
            url {
                parameters.append("lang", Locale.getDefault().language)
            }
        }
    }

    @ClientData
    @Provides
    fun clientData(
        engine: HttpClientEngine
    ): HttpClient = HttpClient(engine) {
        defaultRequest {
            url("https://www.cinemacity.cz/cz/data-api-service/v1")
            url {
                parameters.append("lang", Locale.getDefault().language)
            }
        }
    }

    @ClientCustomer
    @Provides
    fun clientCustomer(
        engine: HttpClientEngine
    ): HttpClient = HttpClient(engine) {
        defaultRequest {
            url("https://www.cinemacity.cz/cz/group-customer-service")
            url {
                parameters.append("lang", Locale.getDefault().language)
            }
        }
    }

    @Provides
    fun engine(): HttpClientEngine = CIO.create()

}