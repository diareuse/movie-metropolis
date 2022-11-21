package movie.metropolis.app.screen.profile

import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import movie.metropolis.app.model.MembershipView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.UrlResponder
import movie.metropolis.app.screen.ViewModelTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

class ProfileViewModelTest : ViewModelTest() {

    private lateinit var viewModel: ProfileViewModel

    override fun prepare() {
        viewModel = ProfileViewModel()
    }

    @Test
    fun membership_returnsValue() = runTest {
        respondWithUser()
        val loadable = viewModel.membership
            .dropWhile { it is Loadable.Loading }
            .first()
        assertIs<Loadable.Loaded<MembershipView?>>(loadable)
        val result = loadable.result
        assertNotNull(result)
    }

    @Test
    fun firstName_returnsValue_ifPreviouslyEmpty() = runTest {
        respondWithUser()
        viewModel.membership.first()
        assertNotEquals("", viewModel.firstName.value)
    }

    @Test
    fun lastName_returnsValue_ifPreviouslyEmpty() = runTest {
        respondWithUser()
        viewModel.membership.first()
        assertNotEquals("", viewModel.lastName.value)
    }

    @Test
    fun email_returnsValue_ifPreviouslyEmpty() = runTest {
        respondWithUser()
        viewModel.membership.first()
        assertNotEquals("", viewModel.email.value)
    }

    @Test
    fun phone_returnsValue_ifPreviouslyEmpty() = runTest {
        respondWithUser()
        viewModel.membership.first()
        assertNotEquals("", viewModel.phone.value)
    }

    @Test
    fun birthDate_returnsValue_ifPreviouslyEmpty() = runTest {
        respondWithUser()
        viewModel.membership.first()
        assertNotEquals(null, viewModel.birthDate.value)
    }

    @Test
    fun favorite_returnsValue_ifPreviouslyEmpty() = runTest {
        respondWithUser()
        viewModel.membership.first()
        assertNotEquals(null, viewModel.favorite.value)
    }

    @Test
    fun hasMarketing_returnsValue_ifPreviouslyEmpty() = runTest {
        respondWithUser()
        viewModel.membership.first()
        assertNotEquals(null, viewModel.hasMarketing.value)
    }

    @Test
    fun save_updatesUser() = runTest {
        respondWithUser()
        viewModel.firstName.value = "barfofofo"
        viewModel.save()
        val loadable = viewModel.membership.first()
        assertIs<Loadable.Loaded<MembershipView?>>(loadable)
        val result = loadable.result
        assertNotNull(result)
    }

    @Test
    fun save_updatesPassword() = runTest {
        responder.onUrlRespond(UrlResponder.Password, "")
        viewModel.passwordCurrent.value = "oof"
        viewModel.passwordNew.value = "foo"
        viewModel.save()
        assertEquals("", viewModel.passwordCurrent.value)
        assertEquals("", viewModel.passwordNew.value)
    }

    // ---

    private fun respondWithUser() {
        responder.onUrlRespond(
            UrlResponder.Customer,
            "group-customer-service-customers-current.json"
        )
        responder.onUrlRespond(
            UrlResponder.CustomerPoints,
            "group-customer-service-customer-points.json"
        )
    }

}