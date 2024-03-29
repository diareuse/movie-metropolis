package movie.metropolis.app.presentation

import movie.calendar.CalendarList
import movie.core.EventCinemaFeature
import movie.core.EventDetailFeature
import movie.core.EventPreviewFeature
import movie.core.EventPromoFeature
import movie.core.EventShowingsFeature
import movie.core.FavoriteFeature
import movie.core.PosterFeature
import movie.core.SetupFeature
import movie.core.UserBookingFeature
import movie.core.UserCredentialFeature
import movie.core.UserDataFeature
import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.model.Movie
import movie.core.preference.EventPreference
import movie.core.preference.SyncPreference
import movie.image.ImageAnalyzer
import movie.image.Swatch
import movie.image.SwatchColor.Companion.Black
import movie.log.Logger
import movie.log.PlatformLogger
import movie.rating.ActorProvider
import movie.rating.MetadataProvider
import org.junit.Before
import org.mockito.kotlin.any
import org.mockito.kotlin.mock

abstract class FeatureTest {

    protected lateinit var rating: MetadataProvider
    protected lateinit var analyzer: ImageAnalyzer
    protected lateinit var promo: EventPromoFeature
    protected lateinit var favorite: FavoriteFeature
    protected lateinit var preview: EventPreviewFeature.Factory
    protected lateinit var showings: EventShowingsFeature.Factory
    protected lateinit var detail: EventDetailFeature
    protected lateinit var cinema: EventCinemaFeature
    protected lateinit var prefs: EventPreference
    protected lateinit var sync: SyncPreference
    protected lateinit var calendars: CalendarList
    protected lateinit var setup: SetupFeature
    protected lateinit var data: UserDataFeature
    protected lateinit var credentials: UserCredentialFeature
    protected lateinit var booking: UserBookingFeature
    protected lateinit var poster: PosterFeature
    protected lateinit var actors: ActorProvider

    abstract fun prepare()

    @Before
    fun prepareInternal() {
        Logger.setLogger(PlatformLogger())
        prepareEvent()
        prepare()
    }

    private fun prepareEvent() {
        preview = object : EventPreviewFeature.Factory {
            private val mock = mock<EventPreviewFeature>()
            override fun current(): EventPreviewFeature = mock
            override fun upcoming(): EventPreviewFeature = mock
        }
        showings = object : EventShowingsFeature.Factory {
            private val cineMock = mock<EventShowingsFeature.Cinema>()
            private val movieMock = mock<EventShowingsFeature.Movie>()
            override fun cinema(cinema: Cinema): EventShowingsFeature.Cinema = cineMock
            override fun movie(movie: Movie, location: Location): EventShowingsFeature.Movie =
                movieMock
        }
        detail = mock()
        actors = mock()
        cinema = mock()
        prefs = mock()
        analyzer = mock {
            onBlocking { getColors(any()) }.thenReturn(Swatch(Black, Black, Black))
        }
        rating = mock {
            onBlocking { get(any()) }.thenReturn(null)
        }
        sync = mock {}
        data = mock()
        credentials = mock()
        booking = mock()
        calendars = mock()
        setup = mock()
        favorite = mock()
        promo = mock {
            onBlocking { get(any()) }.thenReturn(Result.failure(IllegalStateException()))
        }
        poster = mock {}
    }

}