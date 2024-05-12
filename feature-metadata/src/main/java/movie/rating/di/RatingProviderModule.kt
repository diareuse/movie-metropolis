package movie.rating.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
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
import movie.rating.MetadataProviderInvalidateRating
import movie.rating.MetadataProviderStoring
import movie.rating.MetadataProviderTMDB
import movie.rating.database.RatingDao
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class RatingProviderModule {

    @Provides
    @Reusable
    fun rating(
        @Rating client: Provider<HttpClient>,
        dao: RatingDao
    ): MetadataProvider {
        var out: MetadataProvider
        out = MetadataProviderTMDB(client)
        out = MetadataProviderStoring(out, dao)
        out = MetadataProviderFold(
            MetadataProviderInvalidateRating(MetadataProviderDatabase(dao)),
            out,
            MetadataProviderDatabase(dao)
        )
        out = MetadataProviderCatch(out)
        return out
    }

    @Provides
    @Singleton
    internal fun engine() = OkHttp.create()

    @Singleton
    @Provides
    @Rating
    internal fun client(engine: HttpClientEngine): HttpClient = HttpClient(engine) {
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