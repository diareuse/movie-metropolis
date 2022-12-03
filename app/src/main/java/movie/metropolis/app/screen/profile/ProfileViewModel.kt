package movie.metropolis.app.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import movie.metropolis.app.feature.global.EventFeature
import movie.metropolis.app.feature.global.FieldUpdate
import movie.metropolis.app.feature.global.User
import movie.metropolis.app.feature.global.UserFeature
import movie.metropolis.app.model.CinemaSimpleView
import movie.metropolis.app.model.CinemaSimpleViewFromFeature
import movie.metropolis.app.model.MembershipViewFeature
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.StateMachine
import movie.metropolis.app.screen.asLoadable
import movie.metropolis.app.screen.getOrThrow
import movie.metropolis.app.screen.map
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val feature: UserFeature,
    private val event: EventFeature
) : ViewModel() {

    private val stateMachine = StateMachine<Loadable<User>>(viewModelScope, Loadable.loading()) {
        Loadable.loading()
    }

    private val user = stateMachine.state
        .onEachSuccess { user ->
            firstName.update { user.firstName }
            lastName.update { user.lastName }
            email.update { user.email }
            phone.update { user.phone }
            favorite.update { user.favorite?.let(::CinemaSimpleViewFromFeature) }
            hasMarketing.update { user.consent.marketing }
            passwordCurrent.update { "" }
            passwordNew.update { "" }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())

    val isLoggedIn = flow { emit(feature.getToken().isSuccess) }

    val isLoading = stateMachine.state.map { it.isLoading }

    val membership = user
        .map { it.map { user -> user.membership?.let { MembershipViewFeature(user) } } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())

    val cinemas = flow { emit(event.getCinemas(null)) }
        .map { it.map { it.map(::CinemaSimpleViewFromFeature) } }
        .map { it.asLoadable() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())

    val firstName = MutableStateFlow("")
    val lastName = MutableStateFlow("")
    val email = MutableStateFlow("")
    val phone = MutableStateFlow("")
    val favorite = MutableStateFlow(null as CinemaSimpleView?)
    val hasMarketing = MutableStateFlow(null as Boolean?)
    val passwordCurrent = MutableStateFlow("")
    val passwordNew = MutableStateFlow("")

    init {
        stateMachine.submit { feature.getUser().asLoadable() }
    }

    fun save() {
        val user = user.value.getOrNull() ?: return
        val fields = listOfNotNull(
            compare(firstName, user.firstName, FieldUpdate.Name::First),
            compare(lastName, user.lastName, FieldUpdate.Name::Last),
            compare(email, user.email, FieldUpdate::Email),
            compare(phone, user.phone, FieldUpdate::Phone),
            compare(favorite.value?.id, user.favorite?.id) { FieldUpdate.Cinema(it) },
            compare(hasMarketing, user.consent.marketing, FieldUpdate.Consent::Marketing),
            FieldUpdate.Password(passwordCurrent.value, passwordNew.value).takeIf { it.isValid }
        )
        stateMachine.submit {
            feature.update(fields).asLoadable()
        }
    }

    private fun <T : Any> compare(
        updated: StateFlow<T?>,
        remote: T?,
        factory: (T) -> FieldUpdate
    ): FieldUpdate? = compare(
        updated = updated.value,
        remote = remote,
        factory = factory
    )

    private fun <T : Any> compare(
        updated: T?,
        remote: T?,
        factory: (T) -> FieldUpdate
    ): FieldUpdate? {
        if (updated != remote && updated != null) return factory(updated)
        return null
    }

    private fun <T> Flow<Loadable<T>>.onEachSuccess(body: suspend (T) -> Unit) = onEach {
        if (it.isSuccess) body(it.getOrThrow())
    }

}