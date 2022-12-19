package movie.rating.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import movie.rating.LinkProvider
import movie.rating.LinkProviderCaching
import movie.rating.LinkProviderCsfd
import movie.rating.LinkProviderImdb
import movie.rating.LinkProviderRottenTomatoes
import movie.rating.RatingProvider
import movie.rating.RatingProviderCsfd
import movie.rating.RatingProviderFallback
import movie.rating.RatingProviderImdb
import movie.rating.RatingProviderRottenTomatoes

@Module
@InstallIn(SingletonComponent::class)
internal class RatingProviderModule {

    @Provides
    fun rating(
        @Rating client: HttpClient,
        @RottenTomatoes tomatoes: RatingProvider = rtRating(client),
        @Imdb imdb: RatingProvider = imdbRating(client),
        @Csfd csfd: RatingProvider = csfdRating(client),
    ): RatingProvider = RatingProviderFallback(tomatoes, imdb, csfd)

    @Provides
    @Csfd
    fun csfdRating(
        @Rating
        client: HttpClient,
        @Csfd
        link: LinkProvider = csfdLink(client)
    ): RatingProvider =
        RatingProviderCsfd(client, link)

    @Provides
    @Csfd
    fun csfdLink(
        @Rating
        client: HttpClient
    ): LinkProvider {
        var link: LinkProvider
        link = LinkProviderCsfd(client)
        link = LinkProviderCaching(link)
        return link
    }

    @Provides
    @Imdb
    fun imdbRating(
        @Rating
        client: HttpClient,
        @Imdb
        link: LinkProvider = imdbLink(client)
    ): RatingProvider =
        RatingProviderImdb(client, link)

    @Provides
    @Imdb
    fun imdbLink(
        @Rating
        client: HttpClient
    ): LinkProvider {
        var link: LinkProvider
        link = LinkProviderImdb(client)
        link = LinkProviderCaching(link)
        return link
    }

    @Provides
    @RottenTomatoes
    fun rtRating(
        @Rating
        client: HttpClient,
        @RottenTomatoes
        link: LinkProvider = rtLink(client)
    ): RatingProvider =
        RatingProviderRottenTomatoes(client, link)

    @Provides
    @RottenTomatoes
    fun rtLink(
        @Rating
        client: HttpClient
    ): LinkProvider {
        var link: LinkProvider
        link = LinkProviderRottenTomatoes(client)
        link = LinkProviderCaching(link)
        return link
    }

    @Provides
    @Rating
    fun client(): HttpClient = HttpClient(CIO) {}

}