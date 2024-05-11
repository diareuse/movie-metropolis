package movie.metropolis.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import movie.cinema.city.CredentialsProvider
import movie.cinema.city.RegionProvider
import movie.cinema.city.TokenStore
import movie.core.auth.UserAccount
import movie.metropolis.app.feature.cinemacity.CredentialsProviderImpl
import movie.metropolis.app.feature.cinemacity.RegionProviderImpl
import movie.metropolis.app.feature.cinemacity.TokenStoreImpl
import movie.settings.GlobalPreferences

@Module
@InstallIn(SingletonComponent::class)
class CinemaCityModule {

    @Provides
    fun credentials(
        user: UserAccount
    ): CredentialsProvider {
        return CredentialsProviderImpl(user)
    }

    @Provides
    fun region(
        prefs: GlobalPreferences
    ): RegionProvider {
        return RegionProviderImpl(prefs)
    }

    @Provides
    fun token(
        user: UserAccount
    ): TokenStore {
        return TokenStoreImpl(user)
    }

}