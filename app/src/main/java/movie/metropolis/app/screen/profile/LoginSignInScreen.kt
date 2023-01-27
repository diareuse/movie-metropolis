package movie.metropolis.app.screen.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import movie.metropolis.app.ActivityActions
import movie.metropolis.app.LocalActivityActions
import movie.metropolis.app.R
import movie.metropolis.app.screen.profile.component.LoginScreenLayout
import movie.metropolis.app.screen.setup.component.Background
import movie.style.AppButton
import movie.style.AppIconButton
import movie.style.InputField
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
    val state by viewModel.state.collectAsState()
    LaunchedEffect(state) {
        if (state.getOrNull() == true) onNavigateHome()
    }
    LoginSignInScreen(
        email = email,
        password = password,
        error = state.isFailure,
        loading = state.isLoading,
        domain = domain,
        onEmailChanged = { viewModel.email.value = it },
        onPasswordChanged = { viewModel.password.value = it },
        onSendClick = viewModel::send,
        onBackClick = onBackClick
    )
}

@Composable
private fun LoginSignInScreen(
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
    LoginScreenLayout(
        title = { Text(stringResource(R.string.login_title)) },
        onBackClick = onBackClick
    ) { padding ->
        Background(count = 5) {
            when (it % 2) {
                1 -> Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(.2f),
                    painter = painterResource(id = R.drawable.ic_cinema),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Theme.color.container.primary)
                )

                else -> Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(.2f),
                    painter = painterResource(id = R.drawable.ic_movie),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Theme.color.container.secondary)
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(24.dp)
                .imePadding()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.End
        ) {
            InputField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = onEmailChanged,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = error,
                readOnly = loading,
                placeholder = "john.doe@cinema.com",
                label = stringResource(R.string.email),
                supportingText = when (error) {
                    true -> stringResource(R.string.login_error)
                    else -> stringResource(R.string.login_credentials)
                }
            )
            var isHidden by remember { mutableStateOf(true) }
            InputField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = onPasswordChanged,
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (isHidden) KeyboardType.Password else KeyboardType.Text
                ),
                isError = error,
                readOnly = loading,
                placeholder = "p4$\$w0rd",
                label = stringResource(R.string.password),
                supportingText = when (error) {
                    true -> stringResource(R.string.password_failure)
                    else -> null
                },
                trailingIcon = {
                    AppIconButton(
                        painter = painterResource(R.drawable.ic_eye),
                        onClick = { isHidden = !isHidden }
                    )
                }
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(loading) {
                    CircularProgressIndicator(Modifier.scale(.5f))
                }
                AnimatedVisibility(error) {
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