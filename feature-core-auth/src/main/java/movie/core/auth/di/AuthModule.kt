package movie.core.auth.di

import android.accounts.AccountManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import movie.core.auth.AuthMetadata
import movie.core.auth.BuildConfig
import movie.core.auth.CipherFactory
import movie.core.auth.EncryptionProvider
import movie.core.auth.EncryptionProviderAndroid
import movie.core.auth.EncryptionProviderIterator
import movie.core.auth.EncryptionProviderPlaintext
import movie.core.auth.EncryptionProviderRetry
import movie.core.auth.UserAccount
import movie.core.auth.UserAccountImpl

@Module
@InstallIn(SingletonComponent::class)
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
        var provider: EncryptionProvider
        provider = EncryptionProviderIterator(
            EncryptionProviderAndroid(context, CipherFactory.KeySpec.Standard),
            EncryptionProviderAndroid(context, CipherFactory.KeySpec.Leanback),
            EncryptionProviderPlaintext()
        )
        provider = EncryptionProviderRetry(provider)
        return provider
    }

    @Provides
    fun authMeta(): AuthMetadata = AuthMetadata(
        user = BuildConfig.BasicUser,
        password = BuildConfig.BasicPass,
        captcha = BuildConfig.Captcha
    )

}