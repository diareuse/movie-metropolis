package movie.wear.di

import android.content.Context
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.Wearable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import movie.wear.WearService
import movie.wear.WearServiceImpl
import movie.wear.WearServiceUriSpec

@InstallIn(SingletonComponent::class)
@Module
class WearModule {

    @Provides
    fun service(
        client: DataClient
    ): WearService {
        var out: WearService
        out = WearServiceImpl(client)
        out = WearServiceUriSpec(out)
        return out
    }

    @Provides
    internal fun client(
        @ApplicationContext
        context: Context
    ): DataClient {
        return Wearable.getDataClient(context)
    }

}