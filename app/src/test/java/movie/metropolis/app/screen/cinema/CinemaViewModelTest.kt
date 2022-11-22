package movie.metropolis.app.screen.cinema

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import movie.metropolis.app.screen.UrlResponder
import movie.metropolis.app.screen.ViewModelTest
import movie.metropolis.app.screen.getOrThrow
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.hours

class CinemaViewModelTest : ViewModelTest() {

    private lateinit var cinema: String
    private lateinit var viewModel: CinemaViewModel

    override fun prepare() {
        cinema = "1234"
        viewModel = CinemaViewModel(SavedStateHandle(mapOf("cinema" to cinema)))
    }

    @Test
    fun items_returnsList() = runTest {
        responder.onUrlRespond(
            UrlResponder.EventOccurrence(cinema, todayDate),
            "quickbook-film-events-in-cinema-at-date.json"
        )
        val loadable = viewModel.items
            .dropWhile { it.isLoading }
            .first()
        val result = loadable.getOrThrow()
        assertEquals(19, result.size, "Expected 19 elements, but was: $result")
    }

    @Test
    fun items_returnsList_withLaterDate() = runTest {
        responder.onUrlRespond(
            UrlResponder.EventOccurrence(cinema, tomorrowDate),
            "quickbook-film-events-in-cinema-at-date.json"
        )
        viewModel.selectedDate.value = tomorrow
        val loadable = viewModel.items
            .dropWhile { it.isLoading }
            .first()
        val result = loadable.getOrThrow()
        assertEquals(19, result.size, "Expected 19 elements, but was: $result")
    }

    @Test(expected = Throwable::class)
    fun items_failsGracefully() = runTest {
        viewModel.items
            .dropWhile { it.isLoading }
            .first()
            .getOrThrow()
    }

    companion object {

        private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
        private val todayDate = formatter.format(today)
        private val tomorrowDate = formatter.format(tomorrow)

        private val tomorrow get() = Date()
        private val today get() = Date(System.currentTimeMillis() + 24.hours.inWholeMilliseconds)

    }

}