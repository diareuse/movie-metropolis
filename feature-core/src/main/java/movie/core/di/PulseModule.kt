package movie.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import movie.core.EventFeature
import movie.core.FavoriteFeature
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
        @Saving user: UserFeature,
        @ApplicationContext context: Context
    ): Pulse = PulseCombined(
        PulseSavingCurrent(event),
        PulseSavingUpcoming(event),
        PulseSavingShowings(event),
        PulseSavingBookings(user),
        PulseScheduling(context)
    )

    @IntoSet
    @Provides
    fun exactPulseNotificationMovie(
        event: EventFeature,
        info: NotificationInfoProvider,
        favorite: FavoriteFeature
    ): ExactPulse {
        return ExactPulseNotificationMovie(event, info, favorite)
    }

}