package movie.metropolis.app.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import movie.metropolis.app.model.CinemaSimpleView
import movie.metropolis.app.model.UserView
import movie.metropolis.app.model.adapter.UserViewFromView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.onSuccess
import movie.metropolis.app.screen.profile.ProfileFacade.Companion.cinemasFlow
import movie.metropolis.app.screen.profile.ProfileFacade.Companion.isLoggedInFlow
import movie.metropolis.app.screen.profile.ProfileFacade.Companion.membershipFlow
import movie.metropolis.app.screen.profile.ProfileFacade.Companion.userFlow
import movie.metropolis.app.screen.retainStateIn
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val facade: ProfileFacade
) : ViewModel() {

    private val jobEmitter = Channel<suspend () -> Unit>()

    val cinemas = facade.cinemasFlow
        .retainStateIn(viewModelScope, Loadable.loading())
    val membership = facade.membershipFlow
        .retainStateIn(viewModelScope, Loadable.loading())
    val user = facade.userFlow(jobEmitter.consumeAsFlow())
        .onEachSuccess(::populateFields)
        .retainStateIn(viewModelScope, Loadable.loading())
    val isLoggedIn = facade.isLoggedInFlow
        .retainStateIn(viewModelScope, Loadable.loading())

    val firstName = MutableStateFlow("")
    val lastName = MutableStateFlow("")
    val email = MutableStateFlow("")
    val phone = MutableStateFlow("")
    val favorite = MutableStateFlow(null as CinemaSimpleView?)
    val hasMarketing = MutableStateFlow(null as Boolean?)
    val passwordCurrent = MutableStateFlow("")
    val passwordNew = MutableStateFlow("")

    fun save() {
        jobEmitter.trySend {
            facade.save(UserViewFromView(this@ProfileViewModel))
        }
    }

    private fun populateFields(user: UserView) {
        firstName.update { user.firstName }
        lastName.update { user.lastName }
        email.update { user.email }
        phone.update { user.phone }
        favorite.update { user.favorite }
        hasMarketing.update { user.consent.marketing }
        passwordCurrent.update { "" }
        passwordNew.update { "" }
    }

    private fun <T> Flow<Loadable<T>>.onEachSuccess(body: suspend (T) -> Unit) = onEach {
        it.onSuccess { v -> body(v) }
    }

}