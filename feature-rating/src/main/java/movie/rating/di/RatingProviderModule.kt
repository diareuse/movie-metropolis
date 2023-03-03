package movie.rating.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import movie.rating.LazyHttpClient
import movie.rating.LinkProvider
import movie.rating.LinkProviderCsfd
import movie.rating.LinkProviderImdb
import movie.rating.LinkProviderRottenTomatoes
import movie.rating.RatingProvider
import movie.rating.RatingProviderComposed
import movie.rating.RatingProviderCsfd
import movie.rating.RatingProviderImdb
import movie.rating.RatingProviderRottenTomatoes
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class RatingProviderModule {

    @Provides
    fun rating(
        @RottenTomatoes tomatoes: RatingProvider,
        @Imdb imdb: RatingProvider,
        @Csfd csfd: RatingProvider,
    ): RatingProvider.Composed = RatingProviderComposed(
        rtt = tomatoes,
        imdb = imdb,
        csfd = csfd
    )

    @Provides
    @Csfd
    internal fun csfdRating(
        @Rating
        client: LazyHttpClient,
        @Csfd
        link: LinkProvider = csfdLink(client)
    ): RatingProvider =
        RatingProviderCsfd(client, link)

    @Provides
    @Csfd
    internal fun csfdLink(
        @Rating
        client: LazyHttpClient
    ): LinkProvider {
        return LinkProviderCsfd(client)
    }

    @Provides
    @Imdb
    internal fun imdbRating(
        @Rating
        client: LazyHttpClient,
        @Imdb
        link: LinkProvider = imdbLink(client)
    ): RatingProvider =
        RatingProviderImdb(client, link)

    @Provides
    @Imdb
    internal fun imdbLink(
        @Rating
        client: LazyHttpClient
    ): LinkProvider {
        return LinkProviderImdb(client)
    }

    @Provides
    @RottenTomatoes
    internal fun rtRating(
        @Rating
        client: LazyHttpClient,
        @RottenTomatoes
        link: LinkProvider = rtLink(client)
    ): RatingProvider =
        RatingProviderRottenTomatoes(client, link)

    @Provides
    @RottenTomatoes
    internal fun rtLink(
        @Rating
        client: LazyHttpClient
    ): LinkProvider {
        return LinkProviderRottenTomatoes(client)
    }

    @Singleton
    @Provides
    @Rating
    internal fun client(): LazyHttpClient = LazyHttpClient {
        HttpClient(CIO) {
            defaultRequest {
                header(
                    "user-agent",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/109.0"
                )
            }
        }
    }

}