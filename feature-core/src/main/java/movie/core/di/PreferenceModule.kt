package movie.core.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import movie.core.preference.EventPreference
import movie.core.preference.EventPreferenceStored
import movie.core.preference.RegionPreference
import movie.core.preference.RegionPreferenceStored
import movie.core.preference.SyncPreference
import movie.core.preference.SyncPreferenceStored
import movie.core.preference.UserPreference
import movie.core.preference.UserPreferenceStored
import movie.settings.PreferenceStore
import movie.settings.di.Functionality
import movie.settings.di.User

@Module
@InstallIn(SingletonComponent::class)
class PreferenceModule {

    @Provides
    @Reusable
    fun event(
        @Functionality
        store: PreferenceStore
    ): EventPreference {
        return EventPreferenceStored(store)
    }

    @Provides
    @Reusable
    fun region(
        @User
        store: PreferenceStore
    ): RegionPreference {
        return RegionPreferenceStored(store)
    }

    @Provides
    @Reusable
    fun sync(
        @User
        store: PreferenceStore
    ): SyncPreference {
        return SyncPreferenceStored(store)
    }

    @Provides
    @Reusable
    fun user(
        @User
        store: PreferenceStore
    ): UserPreference {
        return UserPreferenceStored(store)
    }

}