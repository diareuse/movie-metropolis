package movie.wear.di

import android.content.Context
import com.google.android.gms.wearable.Wearable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import movie.wear.WearService
import movie.wear.WearServiceImpl

@InstallIn(SingletonComponent::class)
@Module
class WearModule {

    @Provides
    fun service(
        @ApplicationContext
        context: Context
    ): WearService {
        val client = Wearable.getDataClient(context)
        return WearServiceImpl(client)
    }

}