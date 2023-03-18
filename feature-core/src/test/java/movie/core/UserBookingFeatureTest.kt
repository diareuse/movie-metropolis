@file:OptIn(ExperimentalCoroutinesApi::class)

package movie.core

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import movie.calendar.CalendarWriter
import movie.core.db.dao.BookingDao
import movie.core.db.dao.BookingSeatsDao
import movie.core.db.model.BookingStored
import movie.core.di.UserFeatureModule
import movie.core.model.Booking
import movie.core.model.MovieDetail
import movie.core.model.TicketShared
import movie.core.nwk.UserService
import movie.core.nwk.model.BookingResponse
import movie.core.preference.EventPreference
import movie.core.preference.SyncPreference
import movie.core.util.callback
import movie.core.util.thenBlocking
import movie.core.util.wheneverBlocking
import movie.wear.WearService
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.atLeast
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.Date
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours

class UserBookingFeatureTest {

    private lateinit var feature: UserBookingFeature
    private lateinit var wear: WearService
    private lateinit var sync: SyncPreference
    private lateinit var store: TicketStore
    private lateinit var preference: EventPreference
    private lateinit var writer: CalendarWriter.Factory
    private lateinit var detail: EventDetailFeature
    private lateinit var cinema: EventCinemaFeature
    private lateinit var service: UserService
    private lateinit var seats: BookingSeatsDao
    private lateinit var booking: BookingDao

    @Before
    fun prepare() {
        sync = mock {
            on { booking }.thenReturn(Date())
        }
        store = TicketStore()
        preference = mock {}
        writer = object : CalendarWriter.Factory {
            val writer = mock<CalendarWriter> {}
            override fun create(calendarId: String): CalendarWriter = writer
        }
        detail = mock {}
        cinema = mock {}
        service = mock {}
        seats = mock {}
        booking = mock {}
        wear = mock {}
        feature = UserFeatureModule().booking(
            booking,
            seats,
            service,
            cinema,
            detail,
            writer,
            preference,
            store,
            sync,
            wear
        )
    }

    @Test
    fun invalidate_sets_preference() = runTest {
        feature.invalidate()
        verify(sync).booking = Date(0)
    }

    @Test
    fun get_returns_fromNetwork() = runTest {
        cinema_responds_success()
        detail_responds_success()
        val testData = service_responds_success()
        val outputs = feature.get()
        for (output in outputs)
            assertEquals(testData.size, output.getOrThrow().size)
    }

    @Test
    fun get_returns_fromDatabase() = runTest {
        cinema_responds_success()
        detail_responds_success()
        val testData = database_responds_success()
        val outputs = feature.get()
        for (output in outputs)
            assertEquals(testData.size, output.getOrThrow().size)
    }

    @Test
    fun get_stores() = runTest {
        cinema_responds_success()
        detail_responds_success()
        val testData = service_responds_success()
        feature.get()
        verify(booking, atLeast(testData.size)).insertOrUpdate(any())
        verify(seats, atLeastOnce()).insertOrUpdate(any())
        verify(seats, atLeastOnce()).deleteFor(any())
    }

    @Test
    fun get_saves_timestamp() = runTest {
        cinema_responds_success()
        detail_responds_success()
        service_responds_success()
        feature.get()
        verify(sync, atLeastOnce()).booking = any()
    }

    @Test
    fun get_drains_tickets() = runTest {
        val ticket = TicketShared("testId", Date(), "", "", "", "id", emptyList())
        store.add(ticket)
        cinema_responds_success()
        detail_responds_success()
        service_responds_success()
        val results = feature.get().map { it.getOrThrow() }
        assertTrue {
            results.flatten().any { it.id == ticket.id }
        }
    }

    @Test
    fun get_drains_tickets_whenLoggedOff() = runTest {
        val ticket = TicketShared("testId", Date(), "", "", "", "id", emptyList())
        store.add(ticket)
        service_responds_security()
        cinema_responds_success()
        detail_responds_success()
        val results = feature.get().map { it.getOrThrow() }
        assertTrue {
            results.flatten().any { it.id == ticket.id }
        }
    }

    @Test
    fun get_defaults_toNetwork_whenInvalidated() = runTest {
        sync_responds_invalid()
        feature.get()
        verify(service).getBookings()
    }

    @Test
    fun get_splitsData_fromNetwork() = runTest {
        service_responds_success()
        cinema_responds_success()
        detail_responds_success()
        for (output in feature.get()) {
            assertTrue(output.getOrThrow().any { it is Booking.Expired })
            assertTrue(output.getOrThrow().any { it is Booking.Active })
        }
    }

    @Test
    fun get_splitsData_fromDatabase() = runTest {
        database_responds_success()
        cinema_responds_success()
        detail_responds_success()
        for (output in feature.get()) {
            assertTrue(output.getOrThrow().any { it is Booking.Expired })
            assertTrue(output.getOrThrow().any { it is Booking.Active })
        }
    }

    @Test
    fun get_adds_toWearNetwork_fromDatabase() = runTest {
        database_responds_success()
        cinema_responds_success()
        detail_responds_success()
        feature.get()
        verify(wear, atLeastOnce()).send(eq("/bookings"), any())
    }

    @Test
    fun get_adds_toWearNetwork_fromNetwork() = runTest {
        service_responds_success()
        cinema_responds_success()
        detail_responds_success()
        feature.get()
        verify(wear, atLeastOnce()).send(eq("/bookings"), any())
    }

    @Test
    fun get_removes_fromWearNetwork() = runTest {
        database_responds_empty()
        service_responds_empty()
        cinema_responds_success()
        detail_responds_success()
        feature.get().last().getOrThrow()
        verify(wear, atLeastOnce()).remove("/bookings")
    }

    // ---

    private fun sync_responds_invalid() {
        whenever(sync.booking).thenReturn(Date(0))
    }

    private fun detail_responds_success() {
        wheneverBlocking { detail.get(any(), any()) }.thenBlocking {
            callback(1) {
                Result.success(mock<MovieDetail> {
                    on { id }.thenReturn("")
                    on { duration }.thenReturn(2.hours)
                    on { name }.thenReturn("")
                })
            }
        }
    }

    private fun cinema_responds_success() {
        val data = DataPool.Cinemas.all().asSequence()
        wheneverBlocking { cinema.get(anyOrNull()) }.thenReturn(Result.success(data))
    }

    private fun database_responds_success(): List<BookingStored> {
        val data = DataPool.BookingsStored.all()
        val seatData = DataPool.BookingSeatsViews.all()
        wheneverBlocking { booking.selectAll() }.thenReturn(data)
        wheneverBlocking { seats.select(any()) }.thenReturn(seatData)
        return data
    }

    private fun service_responds_success(): List<BookingResponse> {
        val data = DataPool.BookingResponses.all()
        val detail = DataPool.BookingDetailResponses.first()
        wheneverBlocking { service.getBookings() }.thenReturn(Result.success(data))
        wheneverBlocking { service.getBooking(any()) }.thenReturn(Result.success(detail))
        return data
    }

    private fun service_responds_security() {
        wheneverBlocking { service.getBookings() }.thenReturn(Result.failure(SecurityException()))
    }

    private fun service_responds_empty() {
        wheneverBlocking { service.getBookings() }.thenReturn(Result.success(emptyList()))
    }

    private fun database_responds_empty() {
        wheneverBlocking { booking.selectAll() }.thenReturn(emptyList())
        wheneverBlocking { seats.select(any()) }.thenReturn(emptyList())
    }

    private suspend fun UserBookingFeature.get(): List<Result<List<Booking>>> {
        val outputs = mutableListOf<Result<List<Booking>>>()
        get { outputs += it }
        return outputs
    }

}