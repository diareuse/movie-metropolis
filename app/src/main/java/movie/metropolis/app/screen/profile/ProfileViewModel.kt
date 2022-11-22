package movie.metropolis.app.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import movie.metropolis.app.feature.user.FieldUpdate
import movie.metropolis.app.feature.user.User
import movie.metropolis.app.feature.user.UserFeature
import movie.metropolis.app.model.MembershipView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.StateMachine
import movie.metropolis.app.screen.asLoadable
import movie.metropolis.app.screen.getOrThrow
import movie.metropolis.app.screen.map
import java.text.DateFormat
import java.text.NumberFormat
import java.util.Date
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val feature: UserFeature
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
            birthDate.update { user.birthAt }
            favorite.update { user.favoriteCinemaId }
            hasMarketing.update { user.consent.marketing }
            passwordCurrent.update { "" }
            passwordNew.update { "" }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())

    val membership: Flow<Loadable<MembershipView?>> = user
        .onStart { stateMachine.submit { feature.getUser().asLoadable() } }
        .map { it.map { user -> user.membership?.let { MembershipViewFeature(user) } } }

    val firstName = MutableStateFlow("")
    val lastName = MutableStateFlow("")
    val email = MutableStateFlow("")
    val phone = MutableStateFlow("")
    val birthDate = MutableStateFlow(null as Date?)
    val favorite = MutableStateFlow(null as String?)
    val hasMarketing = MutableStateFlow(null as Boolean?)
    val passwordCurrent = MutableStateFlow("")
    val passwordNew = MutableStateFlow("")

    fun save() {
        val user = user.value.getOrNull() ?: return
        val fields = listOfNotNull(
            compare(firstName, user.firstName, FieldUpdate.Name::First),
            compare(lastName, user.lastName, FieldUpdate.Name::Last),
            compare(email, user.email, FieldUpdate::Email),
            compare(phone, user.phone, FieldUpdate::Phone),
            compare(favorite, user.favoriteCinemaId, FieldUpdate::Cinema),
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
    ): FieldUpdate? {
        val value = updated.value
        if (value != remote && value != null) return factory(value)
        return null
    }

    private fun <T> Flow<Loadable<T>>.onEachSuccess(body: suspend (T) -> Unit) = onEach {
        if (it.isSuccess) body(it.getOrThrow())
    }

}

data class MembershipViewFeature(
    private val user: User
) : MembershipView {

    private val date = DateFormat.getDateInstance(DateFormat.MEDIUM)
    private val number = NumberFormat.getNumberInstance()

    private val membership get() = checkNotNull(user.membership)

    override val isExpired: Boolean
        get() = membership.isExpired
    override val cardNumber: String
        get() = membership.cardNumber
    override val memberFrom: String
        get() = date.format(membership.memberFrom)
    override val memberUntil: String
        get() = date.format(membership.memberUntil)
    override val daysRemaining: String
        get() = (membership.memberUntil.time - Date().time).milliseconds.inWholeDays.toString() + "d"
    override val points: String
        get() = number.format(user.points)

}