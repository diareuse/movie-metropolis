@file:OptIn(ExperimentalMaterial3Api::class)

package movie.metropolis.app.screen2.profile

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
import movie.metropolis.app.screen.home.component.rememberUserImage
import movie.style.BackgroundImage
import movie.style.CollapsingTopAppBar
import movie.style.imagePlaceholder
import movie.style.layout.PreviewLayout
import movie.style.layout.alignForLargeScreen
import movie.style.modifier.glow
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
            title = { Text("You") },
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
                    label = { Text("First Name") },
                    placeholder = { Text("John") }
                )
                CommonTextField(
                    modifier = Modifier
                        .weight(1f)
                        .imagePlaceholder(loading),
                    value = state.lastName,
                    onValueChange = { onStateChange(state.copy(lastName = it)) },
                    label = { Text("Last Name") },
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
                    label = { Text("Email") },
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
                    label = { Text("Phone") },
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
                    text = "Marketing",
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
                Text("Save")
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
                    label = { Text("Current Password") },
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
                    label = { Text("New Password") },
                    placeholder = { Text("p4$\$w0rd") },
                    visualTransformation = PasswordVisualTransformation()
                )
            }
            TextButton(
                modifier = Modifier.align(Alignment.End),
                enabled = password.isChanged,
                onClick = onSavePasswordClick
            ) {
                Text("Save")
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
    enabled: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    TextField(
        modifier = modifier.glow(Theme.container.poster),
        value = value,
        onValueChange = onValueChange,
        label = label,
        enabled = enabled,
        placeholder = placeholder,
        supportingText = supportingText,
        maxLines = 1,
        singleLine = true,
        visualTransformation = visualTransformation,
        colors = TextFieldDefaults.colors(
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = Theme.color.container.surface.copy(.2f),
            focusedContainerColor = Theme.color.container.surface.copy(.5f),
            errorContainerColor = Theme.color.container.error.copy(.2f),
            disabledContainerColor = Theme.color.container.surface.copy(.2f)
        ),
        shape = Theme.container.poster,
        trailingIcon = trailingIcon,
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