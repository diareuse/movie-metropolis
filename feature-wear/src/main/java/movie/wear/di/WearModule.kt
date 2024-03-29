package movie.wear.di

import android.content.Context
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.Wearable
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import movie.wear.WearService
import movie.wear.WearServiceCatching
import movie.wear.WearServiceImpl
import movie.wear.WearServiceUriSpec

@InstallIn(SingletonComponent::class)
@Module
class WearModule {

    @Provides
    @Reusable
    fun service(
        client: DataClient
    ): WearService {
        var out: WearService
        out = WearServiceImpl(client)
        out = WearServiceUriSpec(out)
        out = WearServiceCatching(out)
        return out
    }

    @Provides
    @Reusable
    internal fun client(
        @ApplicationContext
        context: Context
    ): DataClient {
        return Wearable.getDataClient(context)
    }

}