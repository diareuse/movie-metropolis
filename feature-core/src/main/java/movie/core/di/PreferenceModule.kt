package movie.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import movie.core.preference.EventPreference
import movie.core.preference.EventPreferenceStored
import movie.settings.PreferenceStore
import movie.settings.di.Functionality

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

}