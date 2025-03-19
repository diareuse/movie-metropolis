package movie.metropolis.app.ui.profile

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.screen.profile.component.rememberUserImage
import movie.metropolis.app.ui.profile.component.TransparentTextField
import movie.style.Image
import movie.style.layout.PreviewLayout
import movie.style.rememberImageState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ProfileScreen(
    animationScope: AnimatedContentScope,
    state: ProfileState,
    onBackClick: () -> Unit,
    onPhoneChange: (String) -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onConsentChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) = ProfileScreenScaffold(
    modifier = modifier.sharedBounds(
        sharedContentState = rememberSharedContentState("profile"),
        animatedVisibilityScope = animationScope,
        clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(24.dp))
    ),
    icon = {
        val image by rememberUserImage(state.userOrNull?.email.orEmpty())
        val state = rememberImageState(image)
        Image(state)
    },
    firstName = {
        TransparentTextField(
            value = state.userOrNull?.firstName.orEmpty(),
            onValueChange = onFirstNameChange,
            label = { Text("First name") },
            placeholder = { Text("John") }
        )
    },
    lastName = {
        TransparentTextField(
            value = state.userOrNull?.lastName.orEmpty(),
            onValueChange = onLastNameChange,
            label = { Text("Last name") },
            placeholder = { Text("Doe") }
        )
    },
    phone = {
        TransparentTextField(
            value = state.userOrNull?.phone.orEmpty(),
            onValueChange = onPhoneChange,
            label = { Text("Phone") },
            placeholder = { Text("+1 (712) 345-6789") }
        )
    },
    cinema = {
        TransparentTextField(
            value = state.userOrNull?.favorite?.name.orEmpty(),
            onValueChange = {},
            readOnly = true,
            label = { Text("Favorite cinema") },
            placeholder = { Text("Cinema") }
        )
    },
    consent = {
        Row {
            Checkbox(state.userOrNull?.consent?.marketing == true, onConsentChange)
            Text("Consent")
        }
    },
    navigationIcon = {
        IconButton(onBackClick) {
            Icon(
                Icons.AutoMirrored.Default.ArrowBack,
                null
            )
        }
    },
)

@OptIn(ExperimentalSharedTransitionApi::class)
@PreviewLightDark
@PreviewFontScale
@Composable
private fun ProfileScreenPreview() = PreviewLayout {
    SharedTransitionLayout {
        AnimatedContent(true) { _ ->
            ProfileScreen(this, ProfileState(), {}, {}, {}, {}, {})
        }
    }
}