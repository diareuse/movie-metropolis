package movie.metropolis.app.screen.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import movie.metropolis.app.R
import movie.style.AppButton
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
    val state by viewModel.state.collectAsState()
    LaunchedEffect(state) {
        if (state.getOrNull() == true) onNavigateHome()
    }
    LoginSignInScreen(
        email = email,
        password = password,
        error = state.isFailure,
        loading = state.isLoading,
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
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onSendClick: () -> Unit,
    onBackClick: () -> Unit
) {
    LoginScreenLayout(
        title = { Text("Welcome back!") },
        onBackClick = onBackClick
    ) { padding ->
        Box(Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier
                    .offset(64.dp, (-16).dp)
                    .size(300.dp)
                    .alpha(.2f)
                    .align(Alignment.TopEnd),
                painter = painterResource(id = R.drawable.ic_cinema),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Theme.color.container.primary)
            )
            Image(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset((-64).dp, 64.dp)
                    .size(300.dp)
                    .alpha(.2f),
                painter = painterResource(id = R.drawable.ic_movie),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Theme.color.container.secondary)
            )
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
                label = "Email"
            )
            InputField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = onPasswordChanged,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = error,
                readOnly = loading,
                placeholder = "p4$\$w0rd",
                label = "Password"
            )
            if (error) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        color = Theme.color.container.error,
                        contentColor = Theme.color.content.error,
                        shape = Theme.container.button
                    ) {
                        Text(
                            text = "Email or Password might be incorrect",
                            modifier = Modifier
                                .padding(vertical = 6.dp, horizontal = 16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(loading) {
                    CircularProgressIndicator(Modifier.scale(.5f))
                }
                AppButton(
                    onClick = onSendClick,
                    enabled = !loading
                ) {
                    Text("Log in")
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
            onEmailChanged = {},
            onPasswordChanged = {},
            onSendClick = {},
            onBackClick = {}
        )
    }
}