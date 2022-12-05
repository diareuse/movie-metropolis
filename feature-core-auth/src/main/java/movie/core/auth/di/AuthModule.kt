package movie.core.auth.di

import android.accounts.AccountManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import movie.core.auth.AuthMetadata
import movie.core.auth.BuildConfig
import movie.core.auth.UserAccount
import movie.core.auth.UserAccountImpl
import movie.core.auth.UserCredentials
import movie.core.auth.UserCredentialsImpl

@Module
@InstallIn(ActivityRetainedComponent::class)
class AuthModule {

    @Provides
    fun account(
        @ApplicationContext
        context: Context,
        credentials: UserCredentials
    ): UserAccount {
        return UserAccountImpl(AccountManager.get(context), credentials)
    }

    @Provides
    fun credentials(
        @ApplicationContext
        context: Context
    ): UserCredentials {
        return UserCredentialsImpl(context)
    }

    @Provides
    fun authMeta(): AuthMetadata = AuthMetadata(
        user = BuildConfig.BasicUser,
        password = BuildConfig.BasicPass,
        captcha = BuildConfig.Captcha
    )

}