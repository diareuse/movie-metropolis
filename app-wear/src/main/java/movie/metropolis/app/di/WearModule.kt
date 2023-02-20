package movie.metropolis.app.di

import android.content.Context
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.Wearable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@InstallIn(ActivityRetainedComponent::class)
@Module
class WearModule {

    @Provides
    fun client(
        @ApplicationContext
        context: Context
    ): DataClient {
        return Wearable.getDataClient(context)
    }

}