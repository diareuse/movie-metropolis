package movie.metropolis.app.feature.user.di

import android.accounts.AccountManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import movie.metropolis.app.di.ClientCustomer
import movie.metropolis.app.feature.global.EventFeature
import movie.metropolis.app.feature.user.UserAccount
import movie.metropolis.app.feature.user.UserAccountImpl
import movie.metropolis.app.feature.user.UserCredentials
import movie.metropolis.app.feature.user.UserCredentialsImpl
import movie.metropolis.app.feature.user.UserFeature
import movie.metropolis.app.feature.user.UserFeatureImpl
import movie.metropolis.app.feature.user.UserService
import movie.metropolis.app.feature.user.UserServiceImpl
import movie.metropolis.app.feature.user.UserServiceReauthorize
import movie.metropolis.app.feature.user.UserServiceSaving

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
        credentials: UserCredentials
    ): UserService {
        var service: UserService
        service = UserServiceImpl(client, account)
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

}