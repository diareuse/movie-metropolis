@file:OptIn(ExperimentalMaterial3Api::class)

package movie.metropolis.app.screen.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import kotlinx.coroutines.launch
import movie.metropolis.app.ActivityActions
import movie.metropolis.app.LocalActivityActions
import movie.metropolis.app.R
import movie.metropolis.app.screen.profile.component.ActionsWithProgress
import movie.metropolis.app.screen.profile.component.EmailField
import movie.metropolis.app.screen.profile.component.PasswordField
import movie.metropolis.app.screen.profile.component.rememberOneTapSaving
import movie.metropolis.app.screen.profile.component.requestOneTapAsState
import movie.style.AppButton
import movie.style.AppIconButton
import movie.style.CollapsingTopAppBar
import movie.style.layout.rememberFocusRequester
import movie.style.theme.Theme

@Composable
fun LoginSignInScreen(
    viewModel: LoginViewModel,
    onNavigateHome: () -> Unit,
    onBackClick: () -> Unit
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val domain = remember(viewModel) { viewModel.domain }
    val state by viewModel.loading.collectAsState()
    val oneTap by requestOneTapAsState()
    val oneTapSave = rememberOneTapSaving()
    val scope = rememberCoroutineScope()
    LaunchedEffect(oneTap) {
        val oneTap = oneTap
        if (oneTap != null) {
            viewModel.email.value = oneTap.userName
            viewModel.password.value = oneTap.password.concatToString()
            viewModel.send(onNavigateHome)
        }
    }
    LoginSignInScreen(
        email = email,
        password = password,
        error = state.isFailure,
        loading = state.isLoading,
        domain = domain,
        onEmailChanged = { viewModel.email.value = it },
        onPasswordChanged = { viewModel.password.value = it },
        onSendClick = {
            viewModel.send {
                scope.launch {
                    oneTapSave.save(email, password)
                    onNavigateHome()
                }
            }
        },
        onBackClick = onBackClick
    )
}

@Composable
fun LoginSignInScreen(
    email: String,
    password: String,
    error: Boolean,
    loading: Boolean,
    domain: String,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onSendClick: () -> Unit,
    onBackClick: () -> Unit,
    actions: ActivityActions = LocalActivityActions.current
) {
    Scaffold(
        topBar = {
            CollapsingTopAppBar(
                title = { Text(stringResource(id = R.string.login_title)) },
                navigationIcon = {
                    AppIconButton(
                        onClick = onBackClick,
                        painter = painterResource(id = R.drawable.ic_back)
                    )
                }
            )
        },
        containerColor = Color.Transparent,
        contentColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.surface)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(24.dp)
                .imePadding()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            val emailRequester = rememberFocusRequester(requestFocus = true)
            val passwordRequester = rememberFocusRequester()
            EmailField(
                modifier = Modifier.focusRequester(emailRequester),
                value = email,
                onValueChange = onEmailChanged,
                error = error,
                readOnly = loading,
                supportingText = {
                    val text = when (error) {
                        true -> stringResource(R.string.login_error)
                        else -> stringResource(R.string.login_credentials)
                    }
                    Text(text)
                },
                imeAction = ImeAction.Next,
                onFocusChangeRequest = { passwordRequester.requestFocus() }
            )
            PasswordField(
                modifier = Modifier.focusRequester(passwordRequester),
                value = password,
                onValueChange = onPasswordChanged,
                error = error,
                readOnly = loading,
                supportingText = (@Composable {
                    Text(stringResource(R.string.password_failure))
                }).takeIf { error },
                imeAction = ImeAction.Done,
                onClickDone = {
                    onSendClick()
                    passwordRequester.freeFocus()
                }
            )
            ActionsWithProgress(
                loading = loading,
                error = error,
                errorAction = {
                    AppButton(
                        onClick = {
                            actions.actionView("$domain/account#/password-reset")
                        },
                        elevation = 0.dp,
                        containerColor = Theme.color.container.error,
                        contentColor = Theme.color.content.error
                    ) {
                        Text(stringResource(R.string.reset_password))
                    }
                }
            ) {
                AppButton(
                    onClick = onSendClick,
                    enabled = !loading
                ) {
                    Text(stringResource(R.string.sign_in))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        LoginSignInScreen(
            email = "foo@my.com",
            password = "password",
            error = true,
            loading = true,
            domain = "",
            onEmailChanged = {},
            onPasswordChanged = {},
            onSendClick = {},
            onBackClick = {}
        )
    }
}