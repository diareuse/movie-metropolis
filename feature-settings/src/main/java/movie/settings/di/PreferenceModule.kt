package movie.settings.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import movie.settings.GlobalPreferences
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
    ): SharedPreferences {
        return SharedPreferencesFactory.user().create(context)
    }

    @Singleton
    @Functionality
    @Provides
    fun functionality(
        @ApplicationContext
        context: Context
    ): SharedPreferences {
        return SharedPreferencesFactory.functionality().create(context)
    }

    @Provides
    fun global(
        @User
        prefs: SharedPreferences
    ): GlobalPreferences {
        return GlobalPreferences(prefs)
    }

}