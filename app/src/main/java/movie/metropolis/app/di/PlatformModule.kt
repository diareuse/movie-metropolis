package movie.metropolis.app.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import movie.metropolis.app.feature.billing.BillingFacade
import movie.metropolis.app.feature.billing.BillingFacadeImpl

@Module
@InstallIn(ActivityRetainedComponent::class)
class PlatformModule {

    @Provides
    fun billing(
        @ApplicationContext context: Context
    ): BillingFacade {
        return BillingFacadeImpl(context)
    }

}