package movie.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import movie.core.EventFeature
import movie.core.UserFeature
import movie.core.pulse.*
import movie.pulse.Pulse
import movie.pulse.PulseCombined
import movie.pulse.di.Daily

@Module
@InstallIn(SingletonComponent::class)
class PulseModule {

    @Daily
    @Provides
    fun pulse(
        @Saving event: EventFeature,
        @Saving user: UserFeature
    ): Pulse = PulseCombined(
        PulseSavingCurrent(event),
        PulseSavingUpcoming(event),
        PulseSavingShowings(event),
        PulseSavingBookings(user)
    )

}