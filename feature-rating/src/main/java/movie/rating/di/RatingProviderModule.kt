package movie.rating.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import kotlinx.coroutines.CoroutineScope
import movie.rating.RatingProvider
import movie.rating.RatingProviderComposed
import movie.rating.RatingProviderCsfd
import movie.rating.RatingProviderDatabase
import movie.rating.RatingProviderFold
import movie.rating.RatingProviderImdb
import movie.rating.RatingProviderRottenTomatoes
import movie.rating.RatingProviderStoring
import movie.rating.database.RatingDao
import movie.rating.internal.LazyHttpClient
import movie.rating.internal.LinkProvider
import movie.rating.internal.LinkProviderCsfd
import movie.rating.internal.LinkProviderImdb
import movie.rating.internal.LinkProviderRottenTomatoes
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
        dao: RatingDao,
        scope: CoroutineScope,
        @Csfd
        link: LinkProvider = csfdLink(client)
    ): RatingProvider {
        var out: RatingProvider
        out = RatingProviderCsfd(client, link)
        out = RatingProviderStoring(out, dao, scope)
        out = RatingProviderFold(RatingProviderDatabase(dao, "csfd.cz"), out)
        return out
    }

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
        dao: RatingDao,
        scope: CoroutineScope,
        @Imdb
        link: LinkProvider = imdbLink(client)
    ): RatingProvider {
        var out: RatingProvider
        out = RatingProviderImdb(client, link)
        out = RatingProviderStoring(out, dao, scope)
        out = RatingProviderFold(RatingProviderDatabase(dao, "imdb.com"), out)
        return out
    }

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
        dao: RatingDao,
        scope: CoroutineScope,
        @RottenTomatoes
        link: LinkProvider = rtLink(client)
    ): RatingProvider {
        var out: RatingProvider
        out = RatingProviderRottenTomatoes(client, link)
        out = RatingProviderStoring(out, dao, scope)
        out = RatingProviderFold(RatingProviderDatabase(dao, "rottentomatoes.com"), out)
        return out
    }

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
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/111.0"
                )
            }
        }
    }

}