package movie.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import movie.core.EventFeature
import movie.core.UserFeature
import movie.core.notification.NotificationInfoProvider
import movie.core.pulse.*
import movie.pulse.ExactPulse
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

    @IntoSet
    @Provides
    fun exactPulseNotificationMovie(
        event: EventFeature,
        info: NotificationInfoProvider
    ): ExactPulse {
        return ExactPulseNotificationMovie(event, info)
    }

}