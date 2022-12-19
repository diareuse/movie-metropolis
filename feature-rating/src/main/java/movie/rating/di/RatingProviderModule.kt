package movie.rating.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import movie.rating.RatingProvider
import movie.rating.RatingProviderFallback
import movie.rating.RatingProviderImdb
import movie.rating.RatingProviderRottenTomatoes

@Module
@InstallIn(SingletonComponent::class)
class RatingProviderModule {

    @Provides
    fun rating(
        @Rating client: HttpClient
    ): RatingProvider {
        return RatingProviderFallback(
            RatingProviderRottenTomatoes(client),
            RatingProviderImdb(client)
        )
    }

    @Provides
    @Rating
    fun client(): HttpClient = HttpClient(CIO) {}

}