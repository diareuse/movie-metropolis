package movie.metropolis.app.screen.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.input.nestedscroll.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import movie.metropolis.app.R
import movie.metropolis.app.model.CinemaSimpleView
import movie.metropolis.app.model.MembershipView
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.onLoading
import movie.metropolis.app.presentation.onSuccess
import movie.metropolis.app.screen.profile.component.ActionsWithProgress
import movie.metropolis.app.screen.profile.component.EmailField
import movie.metropolis.app.screen.profile.component.MembershipCard
import movie.metropolis.app.screen.profile.component.MembershipCardLoading
import movie.metropolis.app.screen.profile.component.PasswordField
import movie.style.AppButton
import movie.style.AppIconButton
import movie.style.AppToolbar
import movie.style.InputField
import movie.style.textPlaceholder
import movie.style.theme.Theme

@Composable
fun UserScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
    state: ScrollState = rememberScrollState()
) {
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
        membership = membership,
        cinemas = cinemas,
        isLoading = isLoading.isLoading,
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
        onSettingsClick = onNavigateToSettings,
        onBackClick = onNavigateBack,
        state = state
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserScreen(
    membership: Loadable<MembershipView?>,
    cinemas: Loadable<List<CinemaSimpleView>>,
    isLoading: Boolean,
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
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    state: ScrollState = rememberScrollState()
) {
    val behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        topBar = {
            AppToolbar(
                title = { Text(stringResource(R.string.user)) },
                navigationIcon = {
                    AppIconButton(
                        onClick = onBackClick,
                        painter = painterResource(id = R.drawable.ic_back)
                    )
                },
                actions = {
                    AppIconButton(
                        onClick = onSettingsClick,
                        painter = painterResource(id = R.drawable.ic_settings)
                    )
                },
                scrollBehavior = behavior
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .nestedScroll(behavior.nestedScrollConnection)
                .verticalScroll(state)
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            membership.onSuccess {
                if (it != null) MembershipCard(
                    firstName = firstName,
                    lastName = lastName,
                    view = it
                )
            }.onLoading {
                MembershipCardLoading()
            }
            Text(stringResource(R.string.user_detail), style = Theme.textStyle.title)
            InputField(
                modifier = Modifier.textPlaceholder(isLoading),
                value = firstName,
                onValueChange = onFirstNameChanged,
                label = { Text(stringResource(R.string.first_name)) }
            )
            InputField(
                modifier = Modifier.textPlaceholder(isLoading),
                value = lastName,
                onValueChange = onLastNameChanged,
                label = { Text(stringResource(R.string.last_name)) }
            )
            EmailField(
                modifier = Modifier.textPlaceholder(isLoading),
                value = email,
                onValueChange = onEmailChanged
            )
            InputField(
                modifier = Modifier.textPlaceholder(isLoading),
                value = phone,
                onValueChange = onPhoneChanged,
                label = { Text(stringResource(R.string.phone)) }
            )
            InputField(
                modifier = Modifier.textPlaceholder(isLoading),
                selected = favorite,
                items = cinemas.getOrNull().orEmpty(),
                converter = { it?.name.orEmpty() },
                label = { Text(stringResource(R.string.favorite_cinema)) }
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
                Text(
                    modifier = Modifier.textPlaceholder(isLoading),
                    text = stringResource(R.string.accept_marketing)
                )
            }
            ActionsWithProgress(
                modifier = Modifier.align(Alignment.End),
                loading = isLoading
            ) {
                AppButton(
                    onClick = onSaveClick,
                    enabled = !isLoading
                ) {
                    Text(stringResource(R.string.save))
                }
            }
            Text(text = stringResource(R.string.security), style = Theme.textStyle.title)
            PasswordField(
                modifier = Modifier.textPlaceholder(isLoading),
                value = passwordCurrent,
                onValueChange = onPasswordCurrentChanged,
                label = { Text(stringResource(R.string.old_password)) }
            )
            PasswordField(
                modifier = Modifier.textPlaceholder(isLoading),
                value = passwordNew,
                onValueChange = onPasswordNewChanged,
                label = { Text(stringResource(R.string.new_password)) }
            )
            ActionsWithProgress(
                modifier = Modifier.align(Alignment.End),
                loading = isLoading
            ) {
                AppButton(
                    onClick = onSaveClick,
                    enabled = !isLoading
                ) {
                    Text(stringResource(R.string.save))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        UserScreen(
            membership = Loadable.loading(),
            cinemas = Loadable.loading(),
            isLoading = true,
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
            onBackClick = {},
            onSettingsClick = {}
        )
    }
}