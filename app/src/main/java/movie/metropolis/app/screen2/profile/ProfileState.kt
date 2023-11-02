package movie.metropolis.app.screen2.profile

import androidx.compose.animation.core.*
import androidx.compose.foundation.interaction.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ProfileState(scope: CoroutineScope) {

    var firstName by mutableStateOf("")
    val firstNameSource = MutableInteractionSource()
    var firstNameBlur by mutableStateOf(0.dp)
        private set

    var lastName by mutableStateOf("")
    val lastNameSource = MutableInteractionSource()
    var lastNameBlur by mutableStateOf(0.dp)
        private set

    var email by mutableStateOf("")
    val emailSource = MutableInteractionSource()
    var emailBlur by mutableStateOf(0.dp)
        private set

    var phone by mutableStateOf("")
    val phoneSource = MutableInteractionSource()
    var phoneBlur by mutableStateOf(0.dp)
        private set

    init {
        scope.launch {
            combine(
                firstNameSource.focusFlow(),
                lastNameSource.focusFlow(),
                emailSource.focusFlow(),
                phoneSource.focusFlow()
            ) { (firstName, lastName, email, phone) ->
                when {
                    firstName -> ActiveField.FirstName
                    lastName -> ActiveField.LastName
                    email -> ActiveField.Email
                    phone -> ActiveField.Phone
                    else -> null
                }
            }.collectLatest { field ->
                animate(ActiveField.FirstName, field, firstNameBlur) { firstNameBlur = it }
                animate(ActiveField.LastName, field, lastNameBlur) { lastNameBlur = it }
                animate(ActiveField.Email, field, emailBlur) { emailBlur = it }
                animate(ActiveField.Phone, field, phoneBlur) { phoneBlur = it }
            }
        }
    }

    private fun CoroutineScope.animate(
        self: ActiveField,
        active: ActiveField?,
        current: Dp,
        update: (Dp) -> Unit
    ) = animate(
        from = current,
        to = if (active == null || self == active) 0.dp else 16.dp,
        update = update
    )

    private fun CoroutineScope.animate(from: Dp, to: Dp, update: (Dp) -> Unit) = launch {
        animate(from.value, to.value) { it, _ ->
            update(it.dp)
        }
    }

    private fun InteractionSource.focusFlow() = interactions
        .filterIsInstance<FocusInteraction>()
        .mapNotNull {
            when (it) {
                is FocusInteraction.Focus -> true
                is FocusInteraction.Unfocus -> false
                else -> null
            }
        }
        .onStart { emit(false) }

    enum class ActiveField {
        FirstName, LastName, Email, Phone
    }

}