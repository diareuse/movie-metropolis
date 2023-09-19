package movie.rating.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.bearerAuth
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import movie.rating.BuildConfig
import movie.rating.MetadataProvider
import movie.rating.MetadataProviderCatch
import movie.rating.MetadataProviderDatabase
import movie.rating.MetadataProviderFold
import movie.rating.MetadataProviderStoring
import movie.rating.MetadataProviderTMDB
import movie.rating.database.RatingDao
import movie.rating.internal.LazyHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class RatingProviderModule {

    @Provides
    fun rating(
        @Rating client: LazyHttpClient,
        dao: RatingDao
    ): MetadataProvider {
        var out: MetadataProvider
        out = MetadataProviderTMDB(client)
        out = MetadataProviderStoring(out, dao)
        out = MetadataProviderFold(
            MetadataProviderDatabase(dao),
            out
        )
        out = MetadataProviderCatch(out)
        return out
    }

    @Singleton
    @Provides
    @Rating
    internal fun client(engine: HttpClientEngine = CIO.create()): LazyHttpClient = LazyHttpClient {
        HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
            defaultRequest {
                bearerAuth(BuildConfig.TMDBToken)
            }
        }
    }

}