package movie.metropolis.app.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import movie.style.layout.PreviewLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    state: ProfileState,
    onBackClick: () -> Unit,
    onPhoneChange: (String) -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onConsentChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) = ProfileScreenScaffold(
    modifier = modifier,
    firstName = { TextField(state.user?.firstName.orEmpty(), onFirstNameChange) },
    lastName = { TextField(state.user?.lastName.orEmpty(), onLastNameChange) },
    phone = { TextField(state.user?.phone.orEmpty(), onPhoneChange) },
    cinema = { TextField(state.user?.favorite?.name.orEmpty(), {}, readOnly = true) },
    consent = {
        Row {
            Checkbox(state.user?.consent?.marketing == true, onConsentChange)
            Text("Consent")
        }
    },
    card = {
        val m = state.membership
        if (m != null) Column {
            Text(m.cardNumber)
            Text(m.daysRemaining)
            Text(m.points)
            Text(m.memberFrom)
            Text(m.memberUntil)
        }
    },
    title = { Text("Profile") },
    navigationIcon = {
        IconButton(onBackClick) {
            Icon(
                Icons.AutoMirrored.Default.ArrowBack,
                null
            )
        }
    },
)

@PreviewLightDark
@PreviewFontScale
@Composable
private fun ProfileScreenPreview() = PreviewLayout {
    ProfileScreen(ProfileState(), {}, {}, {}, {}, {})
}