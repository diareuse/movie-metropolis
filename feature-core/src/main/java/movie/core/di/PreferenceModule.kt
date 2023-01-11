package movie.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import movie.core.preference.EventPreference
import movie.core.preference.EventPreferenceStored
import movie.core.preference.RegionPreference
import movie.core.preference.RegionPreferenceStored
import movie.settings.PreferenceStore
import movie.settings.di.Functionality
import movie.settings.di.User

@Module
@InstallIn(SingletonComponent::class)
class PreferenceModule {

    @Provides
    fun event(
        @Functionality
        store: PreferenceStore
    ): EventPreference {
        return EventPreferenceStored(store)
    }

    @Provides
    fun region(
        @User
        store: PreferenceStore
    ): RegionPreference {
        return RegionPreferenceStored(store)
    }

}