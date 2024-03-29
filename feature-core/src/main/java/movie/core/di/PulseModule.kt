package movie.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import movie.core.EventCinemaFeature
import movie.core.EventDetailFeature
import movie.core.EventPreviewFeature
import movie.core.EventShowingsFeature
import movie.core.FavoriteFeature
import movie.core.UserBookingFeature
import movie.core.notification.NotificationInfoProvider
import movie.core.pulse.ExactPulseNotificationMovie
import movie.core.pulse.PulseSavingBookings
import movie.core.pulse.PulseSavingCurrent
import movie.core.pulse.PulseSavingShowings
import movie.core.pulse.PulseSavingUpcoming
import movie.core.pulse.PulseScheduling
import movie.image.ImageAnalyzer
import movie.pulse.ExactPulse
import movie.pulse.Pulse
import movie.pulse.PulseCombined
import movie.pulse.di.Daily

@Module
@InstallIn(SingletonComponent::class)
class PulseModule {

    @Daily
    @Provides
    @Reusable
    fun pulse(
        preview: EventPreviewFeature.Factory,
        cinema: EventCinemaFeature,
        showing: EventShowingsFeature.Factory,
        user: UserBookingFeature,
        @ApplicationContext context: Context
    ): Pulse = PulseCombined(
        PulseSavingCurrent(preview),
        PulseSavingUpcoming(preview),
        PulseSavingShowings(cinema, showing),
        PulseSavingBookings(user),
        PulseScheduling(context)
    )

    @IntoSet
    @Provides
    fun exactPulseNotificationMovie(
        event: EventDetailFeature,
        info: NotificationInfoProvider,
        favorite: FavoriteFeature,
        image: ImageAnalyzer
    ): ExactPulse {
        return ExactPulseNotificationMovie(event, info, favorite, image)
    }

}