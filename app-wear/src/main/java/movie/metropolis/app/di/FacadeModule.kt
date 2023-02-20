package movie.metropolis.app.di

import com.google.android.gms.wearable.DataClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import movie.metropolis.app.presentation.booking.BookingFacade
import movie.metropolis.app.presentation.booking.BookingFacadeFromService
import movie.metropolis.app.presentation.booking.BookingFacadeRecover
import movie.wear.WearService

@InstallIn(ActivityRetainedComponent::class)
@Module
class FacadeModule {

    @Provides
    fun booking(
        service: WearService,
        client: DataClient
    ): BookingFacade {
        var out: BookingFacade
        out = BookingFacadeFromService(service, client)
        out = BookingFacadeRecover(out)
        return out
    }

}