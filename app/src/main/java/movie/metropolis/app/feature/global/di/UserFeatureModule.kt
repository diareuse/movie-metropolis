package movie.metropolis.app.feature.global.di

import android.accounts.AccountManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import movie.metropolis.app.BuildConfig
import movie.metropolis.app.di.ClientCustomer
import movie.metropolis.app.feature.global.EventFeature
import movie.metropolis.app.feature.global.UserAccount
import movie.metropolis.app.feature.global.UserAccountImpl
import movie.metropolis.app.feature.global.UserCredentials
import movie.metropolis.app.feature.global.UserCredentialsImpl
import movie.metropolis.app.feature.global.UserFeature
import movie.metropolis.app.feature.global.UserFeatureImpl
import movie.metropolis.app.feature.global.UserService
import movie.metropolis.app.feature.global.UserServiceImpl
import movie.metropolis.app.feature.global.UserServiceReauthorize
import movie.metropolis.app.feature.global.UserServiceSaving

@Module
@InstallIn(ActivityRetainedComponent::class)
internal class UserFeatureModule {

    @Provides
    fun feature(
        service: UserService,
        account: UserAccount,
        event: EventFeature
    ): UserFeature {
        return UserFeatureImpl(service, account, event)
    }

    @Provides
    fun service(
        @ClientCustomer
        client: HttpClient,
        account: UserAccount,
        credentials: UserCredentials,
        auth: AuthMetadata
    ): UserService {
        var service: UserService
        service = UserServiceImpl(client, account, auth.user, auth.password, auth.captcha)
        service = UserServiceSaving(service, credentials, account)
        service = UserServiceReauthorize(service, credentials, account)
        return service
    }

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

    data class AuthMetadata(
        val user: String,
        val password: String,
        val captcha: String
    )

}