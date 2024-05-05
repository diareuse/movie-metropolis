package movie.cinema.city.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import movie.cinema.city.CinemaCity
import movie.cinema.city.CinemaCityAuth
import movie.cinema.city.CinemaCityClient
import movie.cinema.city.CinemaCityClientCaching
import movie.cinema.city.CinemaCityClientImpl
import movie.cinema.city.CinemaCityComposition
import movie.cinema.city.CredentialsProvider
import movie.cinema.city.EndpointProvider

@Module
@InstallIn(SingletonComponent::class)
class CinemaCityModule {

    @Provides
    @Reusable
    fun cinemaCity(
        endpoint: EndpointProvider,
        credentials: CredentialsProvider
    ): CinemaCity {
        var client: CinemaCityClient
        client = CinemaCityClientImpl(CinemaCityAuth, endpoint, credentials)
        client = CinemaCityClientCaching(client)
        return CinemaCityComposition(client)
    }

}