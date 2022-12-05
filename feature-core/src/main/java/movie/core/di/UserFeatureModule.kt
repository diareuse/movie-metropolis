package movie.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import movie.core.EventFeature
import movie.core.UserFeature
import movie.core.UserFeatureImpl
import movie.core.nwk.UserService

@Module
@InstallIn(ActivityRetainedComponent::class)
internal class UserFeatureModule {

    @Provides
    fun feature(
        service: UserService,
        event: EventFeature
    ): UserFeature {
        return UserFeatureImpl(service, event)
    }

}