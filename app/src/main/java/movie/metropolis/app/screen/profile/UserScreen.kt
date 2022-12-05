package movie.metropolis.app.screen.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import movie.metropolis.app.model.CinemaSimpleView
import movie.metropolis.app.model.MembershipView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.onLoading
import movie.metropolis.app.screen.onSuccess
import movie.metropolis.app.theme.Theme
import movie.metropolis.app.view.InputField
import movie.metropolis.app.view.textPlaceholder

@Composable
fun UserScreen(
    padding: PaddingValues,
    onNavigateToLogin: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val isLoading by viewModel.user.collectAsState()
    val membership by viewModel.membership.collectAsState()
    val cinemas by viewModel.cinemas.collectAsState()
    val firstName by viewModel.firstName.collectAsState()
    val lastName by viewModel.lastName.collectAsState()
    val email by viewModel.email.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val favorite by viewModel.favorite.collectAsState()
    val hasMarketing by viewModel.hasMarketing.collectAsState()
    val passwordCurrent by viewModel.passwordCurrent.collectAsState()
    val passwordNew by viewModel.passwordNew.collectAsState()
    UserScreen(
        padding = padding,
        membership = membership,
        cinemas = cinemas,
        isLoading = isLoading.isLoading,
        isLoggedOut = isLoggedIn.getOrNull() == false,
        favorite = favorite,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phone = phone,
        hasMarketing = hasMarketing == true,
        passwordCurrent = passwordCurrent,
        passwordNew = passwordNew,
        onFavoriteChanged = { viewModel.favorite.value = it },
        onFirstNameChanged = { viewModel.firstName.value = it },
        onLastNameChanged = { viewModel.lastName.value = it },
        onEmailChanged = { viewModel.email.value = it },
        onPhoneChanged = { viewModel.phone.value = it },
        onHasMarketingChanged = { viewModel.hasMarketing.value = it },
        onPasswordCurrentChanged = { viewModel.passwordCurrent.value = it },
        onPasswordNewChanged = { viewModel.passwordNew.value = it },
        onSaveClick = viewModel::save,
        onLoginClick = onNavigateToLogin
    )
}

@Composable
private fun UserScreen(
    padding: PaddingValues,
    membership: Loadable<MembershipView?>,
    cinemas: Loadable<List<CinemaSimpleView>>,
    isLoading: Boolean,
    isLoggedOut: Boolean,
    favorite: CinemaSimpleView?,
    firstName: String,
    lastName: String,
    email: String,
    phone: String,
    hasMarketing: Boolean,
    passwordCurrent: String,
    passwordNew: String,
    onFavoriteChanged: (CinemaSimpleView) -> Unit,
    onFirstNameChanged: (String) -> Unit,
    onLastNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPhoneChanged: (String) -> Unit,
    onHasMarketingChanged: (Boolean) -> Unit,
    onPasswordCurrentChanged: (String) -> Unit,
    onPasswordNewChanged: (String) -> Unit,
    onSaveClick: () -> Unit,
    onLoginClick: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(padding)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        membership.onSuccess {
            if (it != null) MembershipCard(
                firstName = firstName,
                lastName = lastName,
                cardNumber = it.cardNumber,
                until = it.memberUntil,
                points = it.points
            )
        }.onLoading {
            MembershipCard()
        }
        if (isLoggedOut) {
            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("It seems like you're not currently logged in. You can log in or register using the button below.")
                    Button(onClick = onLoginClick) {
                        Text("Log in now!")
                    }
                }
            }
        } else {
            Text("You in Detail", style = MaterialTheme.typography.titleLarge)
            InputField(
                modifier = Modifier.textPlaceholder(isLoading),
                value = firstName,
                onValueChange = onFirstNameChanged,
                label = "First name"
            )
            InputField(
                modifier = Modifier.textPlaceholder(isLoading),
                value = lastName,
                onValueChange = onLastNameChanged,
                label = "Last name"
            )
            InputField(
                modifier = Modifier.textPlaceholder(isLoading),
                value = email, onValueChange = onEmailChanged,
                label = "Email"
            )
            InputField(
                modifier = Modifier.textPlaceholder(isLoading),
                value = phone,
                onValueChange = onPhoneChanged,
                label = "Phone"
            )
            InputField(
                modifier = Modifier.textPlaceholder(isLoading),
                selected = favorite,
                items = cinemas.getOrNull().orEmpty(),
                converter = { it?.name.orEmpty() }
            ) {
                Text(
                    text = it?.name ?: return@InputField,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onFavoriteChanged(it) }
                        .padding(16.dp, 10.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onHasMarketingChanged(!hasMarketing) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    modifier = Modifier.textPlaceholder(isLoading),
                    checked = hasMarketing,
                    onCheckedChange = onHasMarketingChanged
                )
                Text(modifier = Modifier.textPlaceholder(isLoading), text = "Accept Marketing")
            }
            if (!isLoading) Button(
                modifier = Modifier.align(Alignment.End),
                onClick = onSaveClick
            ) {
                Text("Save")
            }
            Text("Security", style = MaterialTheme.typography.titleLarge)
            InputField(
                modifier = Modifier.textPlaceholder(isLoading),
                value = passwordCurrent,
                onValueChange = onPasswordCurrentChanged,
                label = "Old password"
            )
            InputField(
                modifier = Modifier.textPlaceholder(isLoading),
                value = passwordNew,
                onValueChange = onPasswordNewChanged,
                label = "New password"
            )
            if (!isLoading) Button(
                modifier = Modifier.align(Alignment.End),
                onClick = onSaveClick
            ) {
                Text("Save")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        UserScreen(
            padding = PaddingValues(0.dp),
            membership = Loadable.loading(),
            cinemas = Loadable.loading(),
            isLoading = true,
            isLoggedOut = false,
            favorite = null,
            firstName = "John",
            lastName = "Doe",
            email = "foo@bar.com",
            phone = "+1 800-123423",
            hasMarketing = false,
            passwordCurrent = "",
            passwordNew = "",
            onFavoriteChanged = {},
            onFirstNameChanged = {},
            onLastNameChanged = {},
            onEmailChanged = {},
            onPhoneChanged = {},
            onHasMarketingChanged = {},
            onPasswordCurrentChanged = {},
            onPasswordNewChanged = {},
            onSaveClick = {},
            onLoginClick = {}
        )
    }
}