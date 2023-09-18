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
import movie.rating.MetadataProvider
import movie.rating.MetadataProviderComposed
import movie.rating.MetadataProviderCsfd
import movie.rating.MetadataProviderDatabase
import movie.rating.MetadataProviderFold
import movie.rating.MetadataProviderImdb
import movie.rating.MetadataProviderRottenTomatoes
import movie.rating.MetadataProviderStoring
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
        @RottenTomatoes tomatoes: MetadataProvider,
        @Imdb imdb: MetadataProvider,
        @Csfd csfd: MetadataProvider,
    ): MetadataProvider.Composed = MetadataProviderComposed(
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
    ): MetadataProvider {
        var out: MetadataProvider
        out = MetadataProviderCsfd(client, link)
        out = MetadataProviderStoring(out, dao, scope)
        out = MetadataProviderFold(MetadataProviderDatabase(dao, "csfd.cz"), out)
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
    ): MetadataProvider {
        var out: MetadataProvider
        out = MetadataProviderImdb(client, link)
        out = MetadataProviderStoring(out, dao, scope)
        out = MetadataProviderFold(MetadataProviderDatabase(dao, "imdb.com"), out)
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
    ): MetadataProvider {
        var out: MetadataProvider
        out = MetadataProviderRottenTomatoes(client, link)
        out = MetadataProviderStoring(out, dao, scope)
        out = MetadataProviderFold(MetadataProviderDatabase(dao, "rottentomatoes.com"), out)
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