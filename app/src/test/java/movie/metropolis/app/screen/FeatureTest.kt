package movie.metropolis.app.screen

import movie.calendar.CalendarList
import movie.core.EventCinemaFeature
import movie.core.EventDetailFeature
import movie.core.EventPreviewFeature
import movie.core.EventShowingsFeature
import movie.core.SetupFeature
import movie.core.UserFeature
import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.model.Movie
import movie.core.preference.EventPreference
import movie.log.Logger
import movie.log.PlatformLogger
import org.junit.Before
import org.mockito.kotlin.mock

abstract class FeatureTest {

    protected lateinit var user: UserFeature
    protected lateinit var preview: EventPreviewFeature.Factory
    protected lateinit var showings: EventShowingsFeature.Factory
    protected lateinit var detail: EventDetailFeature
    protected lateinit var cinema: EventCinemaFeature
    protected lateinit var prefs: EventPreference
    protected lateinit var calendars: CalendarList
    protected lateinit var setup: SetupFeature

    abstract fun prepare()

    @Before
    fun prepareInternal() {
        Logger.setLogger(PlatformLogger())
        prepareEvent()
        prepareUser()
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
        cinema = mock()
        prefs = mock()
    }

    private fun prepareUser() {
        user = mock()
        calendars = mock()
        setup = mock()
    }

}