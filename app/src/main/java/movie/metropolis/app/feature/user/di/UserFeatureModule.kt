package movie.metropolis.app.feature.user.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import io.ktor.client.HttpClient
import movie.metropolis.app.feature.user.UserFeature

@Module
@InstallIn(ActivityRetainedComponent::class)
class UserFeatureModule {

    @Provides
    fun feature(client: HttpClient): UserFeature {
        TODO()
    }

}