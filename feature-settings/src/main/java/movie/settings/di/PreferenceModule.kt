package movie.settings.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import movie.settings.PreferenceStore
import movie.settings.PreferenceStoreShared
import movie.settings.SharedPreferencesFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PreferenceModule {

    @Singleton
    @User
    @Provides
    fun user(
        @ApplicationContext
        context: Context
    ): PreferenceStore {
        val prefs = SharedPreferencesFactory.user().create(context)
        return PreferenceStoreShared(prefs)
    }

    @Singleton
    @Functionality
    @Provides
    fun functionality(
        @ApplicationContext
        context: Context
    ): PreferenceStore {
        val prefs = SharedPreferencesFactory.functionality().create(context)
        return PreferenceStoreShared(prefs)
    }

}