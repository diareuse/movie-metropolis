package movie.metropolis.app.presentation.settings

import kotlinx.coroutines.test.runTest
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.presentation.FeatureTest
import org.junit.Test
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.concurrent.CountDownLatch
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SettingsFacadeTest : FeatureTest() {

    private lateinit var facade: SettingsFacade

    override fun prepare() {
        facade = FacadeModule().settings(prefs, calendars)
    }

    @Test
    fun filterSeen_returns_true() {
        whenever(prefs.filterSeen).thenReturn(true)
        assertTrue(facade.filterSeen)
    }

    @Test
    fun filterSeen_returns_false() {
        whenever(prefs.filterSeen).thenReturn(false)
        assertFalse(facade.filterSeen)
    }

    @Test
    fun onlyMovies_returns_true() {
        whenever(prefs.onlyMovies).thenReturn(true)
        assertTrue(facade.onlyMovies)
    }

    @Test
    fun onlyMovies_returns_false() {
        whenever(prefs.onlyMovies).thenReturn(false)
        assertFalse(facade.onlyMovies)
    }

    @Test
    fun filterSeen_returns_false_onError() {
        whenever(prefs.filterSeen).thenThrow(RuntimeException())
        assertFalse(facade.filterSeen)
    }

    @Test
    fun addToCalendar_returns_true() {
        whenever(prefs.calendarId).thenReturn("aa")
        assertTrue(facade.addToCalendar)
    }

    @Test
    fun getCalendars_returns_list() = runTest {
        whenever(calendars.query()).thenReturn(emptyList())
        assertEquals(emptyMap(), facade.getCalendars())
    }

    @Test
    fun selectCalendar_setsValue() = runTest {
        facade.selectCalendar("aa")
        verify(prefs).calendarId = "aa"
    }

    @Test
    fun listener_notifies() {
        val latch = CountDownLatch(1)
        facade.addListener {
            latch.countDown()
        }
        facade.filterSeen = false
        latch.await()
    }

    @Test
    fun listener_removes() {
        var value = "success"
        val listener = facade.addListener {
            value = "failure"
        }
        facade.removeListener(listener)
        facade.filterSeen = false
        assertEquals("success", value)
    }

}