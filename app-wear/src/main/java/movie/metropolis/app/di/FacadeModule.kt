package movie.metropolis.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import movie.metropolis.app.presentation.booking.BookingsFacade
import movie.metropolis.app.presentation.booking.BookingsFacadeFromService
import movie.metropolis.app.presentation.booking.BookingsFacadeRecover
import movie.wear.WearService

@InstallIn(ActivityRetainedComponent::class)
@Module
class FacadeModule {

    @Provides
    fun booking(
        service: WearService
    ): BookingsFacade {
        var out: BookingsFacade
        out = BookingsFacadeFromService(service)
        out = BookingsFacadeRecover(out)
        return out
    }

}