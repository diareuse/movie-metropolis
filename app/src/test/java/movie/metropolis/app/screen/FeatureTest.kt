package movie.metropolis.app.screen

import movie.calendar.CalendarList
import movie.core.EventFeature
import movie.core.UserFeature
import movie.core.preference.EventPreference
import movie.log.Logger
import movie.log.PlatformLogger
import org.junit.Before
import org.mockito.kotlin.mock

abstract class FeatureTest {

    protected lateinit var user: UserFeature
    protected lateinit var event: EventFeature
    protected lateinit var prefs: EventPreference
    protected lateinit var calendars: CalendarList

    abstract fun prepare()

    @Before
    fun prepareInternal() {
        Logger.setLogger(PlatformLogger())
        prepareEvent()
        prepareUser()
        prepare()
    }

    private fun prepareEvent() {
        event = mock()
        prefs = mock()
    }

    private fun prepareUser() {
        user = mock()
        calendars = mock()
    }

}