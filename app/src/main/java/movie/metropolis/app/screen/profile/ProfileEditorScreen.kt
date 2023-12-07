@file:OptIn(ExperimentalMaterial3Api::class)

package movie.metropolis.app.screen.profile

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.nestedscroll.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.R
import movie.metropolis.app.screen.profile.component.rememberUserImage
import movie.style.BackgroundImage
import movie.style.CollapsingTopAppBar
import movie.style.imagePlaceholder
import movie.style.layout.PreviewLayout
import movie.style.layout.alignForLargeScreen
import movie.style.rememberImageState
import movie.style.theme.Theme

@Composable
fun ProfileEditorScreen(
    state: ProfileEditorState,
    password: ProfilePasswordState,
    loading: Boolean,
    onStateChange: (ProfileEditorState) -> Unit,
    onPasswordChange: (ProfilePasswordState) -> Unit,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onSaveStateClick: () -> Unit,
    onSavePasswordClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
) = Scaffold(
    modifier = modifier,
    topBar = {
        CollapsingTopAppBar(
            modifier = Modifier.alignForLargeScreen(),
            title = { Text(stringResource(R.string.user)) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(painterResource(R.drawable.ic_back), null)
                }
            },
            scrollBehavior = scrollBehavior
        )
    }
) { padding ->
    Box(
        modifier = Modifier.fillMaxSize(),
        propagateMinConstraints = true
    ) {
        val imageUrl by rememberUserImage(email = state.email)
        val background = rememberImageState(url = imageUrl)
        BackgroundImage(state = background)
        Column(
            modifier = Modifier
                .alignForLargeScreen()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(24.dp)
                .imePadding()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painterResource(R.drawable.ic_person), null)
                CommonTextField(
                    modifier = Modifier
                        .weight(1f)
                        .imagePlaceholder(loading),
                    value = state.firstName,
                    onValueChange = { onStateChange(state.copy(firstName = it)) },
                    label = { Text(stringResource(R.string.first_name)) },
                    placeholder = { Text("John") }
                )
                CommonTextField(
                    modifier = Modifier
                        .weight(1f)
                        .imagePlaceholder(loading),
                    value = state.lastName,
                    onValueChange = { onStateChange(state.copy(lastName = it)) },
                    label = { Text(stringResource(R.string.last_name)) },
                    placeholder = { Text("Doe") }
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painterResource(R.drawable.ic_email), null)
                CommonTextField(
                    modifier = Modifier
                        .weight(1f)
                        .imagePlaceholder(loading),
                    value = state.email,
                    onValueChange = { onStateChange(state.copy(email = it)) },
                    label = { Text(stringResource(R.string.email)) },
                    placeholder = { Text("em@il.com") }
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painterResource(R.drawable.ic_phone), null)
                CommonTextField(
                    modifier = Modifier
                        .weight(1f)
                        .imagePlaceholder(loading),
                    value = state.phone,
                    onValueChange = { onStateChange(state.copy(phone = it)) },
                    label = { Text(stringResource(R.string.phone)) },
                    placeholder = { Text("+420 703 000 000") }
                )
            }
            Row(
                modifier = Modifier.heightIn(min = 56.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painterResource(R.drawable.ic_star), null)
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                        .alpha(.75f),
                    text = stringResource(R.string.accept_marketing),
                    style = Theme.textStyle.emphasis
                )
                CommonSwitch(
                    modifier = Modifier.imagePlaceholder(loading),
                    checked = state.marketingMessaging,
                    onCheckedChange = { onStateChange(state.copy(marketingMessaging = it)) }
                )
            }
            TextButton(
                modifier = Modifier.align(Alignment.End),
                enabled = state.isChanged,
                onClick = onSaveStateClick
            ) {
                Text(stringResource(R.string.save))
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painterResource(R.drawable.ic_password), null)
                CommonTextField(
                    modifier = Modifier.weight(1f),
                    value = password.current,
                    onValueChange = { onPasswordChange(password.copy(current = it)) },
                    label = { Text(stringResource(R.string.old_password)) },
                    placeholder = { Text("p4$\$w0rd") },
                    visualTransformation = PasswordVisualTransformation()
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painterResource(R.drawable.ic_password), null)
                CommonTextField(
                    modifier = Modifier.weight(1f),
                    value = password.new,
                    onValueChange = { onPasswordChange(password.copy(new = it)) },
                    label = { Text(stringResource(R.string.new_password)) },
                    placeholder = { Text("p4$\$w0rd") },
                    visualTransformation = PasswordVisualTransformation()
                )
            }
            TextButton(
                modifier = Modifier.align(Alignment.End),
                enabled = password.isChanged,
                onClick = onSavePasswordClick
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
}

@Composable
fun CommonSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) = Switch(
    modifier = modifier,
    checked = checked,
    onCheckedChange = onCheckedChange,
    colors = SwitchDefaults.colors(
        uncheckedBorderColor = Color.Transparent,
        checkedBorderColor = Color.Transparent,
        disabledCheckedBorderColor = Color.Transparent,
        disabledUncheckedBorderColor = Color.Transparent
    )
)

@Composable
fun CommonTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    supportingText: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = label,
        enabled = enabled,
        placeholder = placeholder,
        supportingText = supportingText,
        maxLines = 1,
        singleLine = true,
        isError = isError,
        visualTransformation = visualTransformation,
        colors = TextFieldDefaults.colors(
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = Theme.color.container.surface.copy(.5f),
            focusedContainerColor = Theme.color.container.surface.copy(.7f),
            errorContainerColor = Theme.color.container.error.copy(.3f),
            disabledContainerColor = Theme.color.container.surface.copy(.3f)
        ),
        shape = Theme.container.poster,
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun ProfileEditorScreenPreview() = PreviewLayout {
    ProfileEditorScreen(
        state = ProfileEditorState(),
        password = ProfilePasswordState(),
        loading = false,
        onPasswordChange = {},
        onStateChange = {},
        onBackClick = {},
        onSavePasswordClick = {},
        onSaveStateClick = {}
    )
}