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
import movie.core.auth.EncryptionProvider
import movie.core.auth.EncryptionProviderAndroid
import movie.core.auth.UserAccount
import movie.core.auth.UserAccountImpl

@Module
@InstallIn(ActivityRetainedComponent::class)
class AuthModule {

    @Provides
    fun account(
        @ApplicationContext
        context: Context,
        encryption: EncryptionProvider
    ): UserAccount {
        return UserAccountImpl(AccountManager.get(context), encryption)
    }

    @Provides
    fun encryption(
        @ApplicationContext
        context: Context
    ): EncryptionProvider {
        return EncryptionProviderAndroid(context)
    }

    @Provides
    fun authMeta(): AuthMetadata = AuthMetadata(
        user = BuildConfig.BasicUser,
        password = BuildConfig.BasicPass,
        captcha = BuildConfig.Captcha
    )

}