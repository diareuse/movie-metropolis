package movie.metropolis.app.screen

import movie.core.EventFeature
import movie.core.UserFeature
import movie.core.preference.EventPreference
import org.junit.Before
import org.mockito.kotlin.mock

abstract class FeatureTest {

    protected lateinit var user: UserFeature
    protected lateinit var event: EventFeature
    protected lateinit var prefs: EventPreference

    abstract fun prepare()

    @Before
    fun prepareInternal() {
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
    }

}