package movie.core

import kotlinx.coroutines.test.runTest
import movie.core.di.UserFeatureModule
import movie.core.model.FieldUpdate
import movie.core.nwk.UserService
import movie.core.nwk.model.CustomerResponse
import movie.core.preference.UserPreference
import movie.core.util.wheneverBlocking
import movie.log.Logger
import movie.log.PlatformLogger
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.Date
import kotlin.random.Random.Default.nextBoolean
import kotlin.random.Random.Default.nextInt

class UserDataFeatureTest {

    private lateinit var preference: UserPreference
    private lateinit var cinema: EventCinemaFeature
    private lateinit var service: UserService
    private lateinit var feature: UserDataFeature

    @Before
    fun prepare() {
        Logger.setLogger(PlatformLogger())
        service = mock {}
        cinema = mock {}
        preference = mock {}
        feature = UserFeatureModule().data(service, cinema, preference)
    }

    @Test
    fun update_writes_favorite() = runTest {
        service_responds_success()
        val expected = "expected${nextInt(1, 100)}"
        feature.update(listOf(FieldUpdate.Cinema(expected)))
        verify(preference, atLeastOnce()).favorite = expected
    }

    @Test
    fun update_writes_marketing() = runTest {
        service_responds_success()
        val expected = nextBoolean()
        feature.update(listOf(FieldUpdate.Consent.Marketing(expected)))
        verify(preference, atLeastOnce()).consentMarketing = expected
    }

    @Test
    fun update_writes_premium() = runTest {
        service_responds_success()
        val expected = nextBoolean()
        feature.update(listOf(FieldUpdate.Consent.Premium(expected)))
        verify(preference, atLeastOnce()).consentPremium = expected
    }

    @Test
    fun update_writes_email() = runTest {
        service_responds_success()
        val expected = "expected${nextInt(1, 100)}"
        feature.update(listOf(FieldUpdate.Email(expected)))
        verify(preference, atLeastOnce()).email = expected
    }

    @Test
    fun update_writes_firstName() = runTest {
        service_responds_success()
        val expected = "expected${nextInt(1, 100)}"
        feature.update(listOf(FieldUpdate.Name.First(expected)))
        verify(preference, atLeastOnce()).firstName = expected
    }

    @Test
    fun update_writes_lastName() = runTest {
        service_responds_success()
        val expected = "expected${nextInt(1, 100)}"
        feature.update(listOf(FieldUpdate.Name.Last(expected)))
        verify(preference, atLeastOnce()).lastName = expected
    }

    @Test
    fun update_writes_phone() = runTest {
        service_responds_success()
        val expected = "expected${nextInt(1, 100)}"
        feature.update(listOf(FieldUpdate.Phone(expected)))
        verify(preference, atLeastOnce()).phone = expected
    }

    @Test
    fun get_writes_user() = runTest {
        cinema_responds_success()
        service_responds_success()
        feature.get()
        verify(preference, atLeastOnce()).set(any())
    }

    @Test
    fun get_returns_fromNetwork() = runTest {
        cinema_responds_success()
        service_responds_success()
        feature.get().getOrThrow()
    }

    @Test
    fun get_returns_fromStored() = runTest {
        cinema_responds_success()
        stored_responds_success()
        feature.get().getOrThrow()
    }

    // ---

    private fun stored_responds_success() {
        whenever(preference.firstName).thenReturn("")
        whenever(preference.lastName).thenReturn("")
        whenever(preference.email).thenReturn("")
        whenever(preference.phone).thenReturn("")
        whenever(preference.favorite).thenReturn(null)
        whenever(preference.points).thenReturn(0.0)
        whenever(preference.consentMarketing).thenReturn(false)
        whenever(preference.consentPremium).thenReturn(false)
        whenever(preference.cardNumber).thenReturn("")
        whenever(preference.memberFrom).thenReturn(Date())
        whenever(preference.memberUntil).thenReturn(Date())
    }

    private fun service_responds_success(): CustomerResponse.Customer {
        val result = DataPool.CustomerResponseCustomer.first()
        val points = DataPool.CustomerPointsResponses.first()
        wheneverBlocking { service.getUser() }.thenReturn(Result.success(result))
        wheneverBlocking { service.getPoints() }.thenReturn(Result.success(points))
        return result
    }

    private fun cinema_responds_success() {
        val data = DataPool.Cinemas.all().asSequence()
        wheneverBlocking { cinema.get(anyOrNull()) }.thenReturn(data)
    }

}