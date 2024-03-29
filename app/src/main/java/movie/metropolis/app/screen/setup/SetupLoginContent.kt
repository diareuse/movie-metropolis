@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalLayoutApi::class)

package movie.metropolis.app.screen.setup

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.focus.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import movie.metropolis.app.R
import movie.metropolis.app.screen.profile.CommonTextField
import movie.metropolis.app.screen.setup.component.rememberRandomItemAsState
import movie.style.BackgroundImage
import movie.style.layout.PreviewLayout
import movie.style.layout.alignForLargeScreen
import movie.style.rememberImageState
import movie.style.theme.Theme

@Composable
fun SetupLoginContent(
    posters: ImmutableList<String>,
    state: LoginState,
    onStateChange: (LoginState) -> Unit,
    onLoginClick: () -> Unit,
    onLoginSkip: () -> Unit,
    modifier: Modifier = Modifier,
) = Box(
    modifier = modifier,
    propagateMinConstraints = true
) {
    val poster by posters.rememberRandomItemAsState()
    BackgroundImage(state = rememberImageState(url = poster))
    Column(
        modifier = Modifier.alignForLargeScreen(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Column {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .alignByBaseline()
                        .alpha(.5f),
                    text = stringResource(R.string.app_name),
                    style = Theme.textStyle.title
                )
                Text(
                    modifier = Modifier
                        .alignByBaseline()
                        .alpha(.5f),
                    text = stringResource(R.string.for_word),
                    style = Theme.textStyle.caption
                )
            }
            Image(
                modifier = Modifier.height(100.dp),
                painter = painterResource(R.drawable.ic_logo_cinemacity),
                contentDescription = null,
                contentScale = ContentScale.FillHeight
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnimatedVisibility(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                visible = state.loading
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    Text(stringResource(R.string.login_loading))
                }
            }
            val reqs = remember { List(2) { FocusRequester() } }
            LaunchedEffect(reqs) {
                reqs[0].requestFocus()
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painterResource(R.drawable.ic_email), null)
                CommonTextField(
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(reqs[0])
                        .focusProperties {
                            next = reqs[1]
                        },
                    value = state.email,
                    onValueChange = { onStateChange(state.copy(email = it)) },
                    isError = state.error,
                    enabled = !state.loading,
                    label = { Text(stringResource(R.string.email)) },
                    placeholder = { Text("john.doe@email.com") },
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Email
                    )
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painterResource(R.drawable.ic_password), null)
                CommonTextField(
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(reqs[1])
                        .focusProperties {
                            previous = reqs[0]
                        },
                    value = state.password,
                    onValueChange = { onStateChange(state.copy(password = it)) },
                    isError = state.error,
                    enabled = !state.loading,
                    label = { Text(stringResource(R.string.password)) },
                    placeholder = { Text("*****") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardActions = KeyboardActions(onDone = { onLoginClick() }),
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    supportingText = {
                        AnimatedVisibility(state.error) {
                            Text(stringResource(R.string.login_error))
                        }
                    }
                )
            }
            if (!WindowInsets.isImeVisible) {
                HorizontalDivider(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .width(100.dp)
                        .align(Alignment.CenterHorizontally)
                )
                TextButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = onLoginSkip,
                    enabled = !state.loading,
                ) {
                    Text(stringResource(R.string.continue_without_login))
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun SetupLoginContentPreview(
    @PreviewParameter(SetupLoginContentParameter::class)
    state: LoginState
) = PreviewLayout(modifier = Modifier.fillMaxSize()) {
    SetupLoginContent(
        posters = List(10) { "" }.toImmutableList(),
        state = state,
        onStateChange = {},
        onLoginClick = {},
        onLoginSkip = {}
    )
}

private class SetupLoginContentParameter :
    PreviewParameterProvider<LoginState> {
    override val values = sequence {
        yield(LoginState())
        yield(LoginState(error = true))
        yield(LoginState(loading = true))
    }

    class Data
}