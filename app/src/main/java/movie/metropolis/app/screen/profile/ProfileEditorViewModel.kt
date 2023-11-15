package movie.metropolis.app.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import movie.metropolis.app.model.adapter.UserViewFromView
import movie.metropolis.app.presentation.profile.ProfileFacade
import movie.metropolis.app.presentation.profile.ProfileFacade.Companion.getMembership
import movie.metropolis.app.presentation.profile.ProfileFacade.Companion.getUser
import javax.inject.Inject

@HiltViewModel
class ProfileEditorViewModel @Inject constructor(
    private val facade: ProfileFacade
) : ViewModel() {

    val state = MutableStateFlow(ProfileEditorState())
    val password = MutableStateFlow(ProfilePasswordState())
    val loading = MutableStateFlow(true)

    init {
        viewModelScope.launch {
            try {
                val user = facade.getUser().getOrNull() ?: return@launch
                state.update {
                    ProfileEditorState(
                        firstName = user.firstName,
                        lastName = user.lastName,
                        email = user.email,
                        phone = user.phone,
                        marketingMessaging = user.consent.marketing,
                        original = null
                    )
                }
            } finally {
                loading.value = false
            }
        }
    }

    fun update(state: ProfileEditorState) {
        this.state.update {
            if (state.isChanged) state else state.copy(original = it)
        }
    }

    fun update(state: ProfilePasswordState) {
        this.password.update { state }
    }

    fun saveState() = viewModelScope.launch {
        val user = facade.getUser().getOrNull() ?: return@launch
        val membership = facade.getMembership().getOrNull()
        try {
            facade.save(UserViewFromView(user, membership, this@ProfileEditorViewModel))
            state.update { it.copy(original = null) }
        } catch (ignore: Throwable) {
        }
    }.let {}

    fun savePassword() = viewModelScope.launch {
        val (old, new) = password.value
        try {
            facade.save(old, new)
            password.update { ProfilePasswordState() }
        } catch (ignore: Throwable) {
        }
    }.let {}

}