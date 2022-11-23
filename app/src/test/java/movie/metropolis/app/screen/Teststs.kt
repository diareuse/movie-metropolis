package movie.metropolis.app.screen

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class FailingSample {
    private lateinit var channel: Channel<Any>
    private lateinit var scope: TestScope
    private lateinit var flow: StateFlow<String>

    @Before
    fun prepare() {
        scope = TestScope()
        channel = Channel()
        flow = channel.consumeAsFlow()
            .map { delay(500); "hello!" } // this fails every time
//            .map { runBlocking { delay(500); "hello!"} }
            .stateIn(scope, SharingStarted.WhileSubscribed(1000L, 1000L), "not hello")
    }

    @Test
    fun shouldNotFail() = scope.runTest {
        assertEquals("not hello", flow.value)
        channel.send("fooo")
        advanceTimeBy(600)
        assertEquals("hello!", flow.value)
        //scope.cancel(":)")
    }
}

class FailingSample2 {

    private lateinit var channel: Channel<Any>
    private lateinit var scope: TestScope
    private lateinit var client: HttpClient
    private lateinit var flow: Flow<String>

    @Before
    fun prepare() {
        val engine = MockEngine { respondOk("hello!") }
        scope = TestScope()
        client = HttpClient(engine) {}
        channel = Channel(1)
        flow = channel.consumeAsFlow()
            .map { client.get {}.bodyAsText() } // this fails every time
        //.map { runBlocking { client.get {}.bodyAsText() } } // this passes the test
        //.stateIn(scope, SharingStarted.Eagerly, "not hello")
    }

    @Test
    fun shouldNotFail() = scope.runTest {
        //assertEquals("not hello", flow.first())
        channel.send("fooo")
        assertEquals("hello!", flow.first())
    }

}