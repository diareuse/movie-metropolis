package movie.metropolis.app.ui.profile.component

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.text.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.*
import movie.style.layout.PreviewLayout

private val userName = KeyboardOptions(
    KeyboardCapitalization.Words,
    autoCorrectEnabled = false,
    keyboardType = KeyboardType.Text,
    imeAction = ImeAction.Done
)
private val password = KeyboardOptions(
    KeyboardCapitalization.None,
    autoCorrectEnabled = false,
    keyboardType = KeyboardType.Password,
    imeAction = ImeAction.Done
)
val KeyboardOptions.Companion.UserName get() = userName
val KeyboardOptions.Companion.Password get() = password

@Composable
fun TransparentTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    maxLines: Int = 1,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        shape = RectangleShape,
        visualTransformation = when (keyboardOptions.keyboardType) {
            KeyboardType.Password,
            KeyboardType.NumberPassword -> PasswordVisualTransformation()

            else -> VisualTransformation.None
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Transparent,
            errorBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent
        ),
        singleLine = maxLines == 1,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        prefix = prefix,
        suffix = suffix,
        supportingText = supportingText,
        isError = isError,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        maxLines = maxLines,
        minLines = minLines,
        interactionSource = interactionSource,
    )
}

@PreviewLightDark
@PreviewFontScale
@Composable
private fun TransparentTextFieldPreview() = PreviewLayout {
    TransparentTextField(
        modifier = Modifier.background(Color.Red.copy(.5f), MaterialTheme.shapes.medium),
        value = "content",
        onValueChange = {},
        placeholder = { Text("Placeholder") },
        supportingText = { Text("Supporting text") },
        label = { Text("Label") }
    )
}