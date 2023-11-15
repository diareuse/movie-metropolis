package movie.metropolis.app.screen.settings.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.*
import movie.style.layout.PreviewLayout
import movie.style.modifier.glow
import movie.style.theme.Theme

@Composable
fun SettingsTextField(
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) = TextField(
    modifier = modifier
        .fillMaxWidth()
        .glow(Theme.container.button),
    value = value,
    onValueChange = onValueChange,
    shape = Theme.container.button,
    colors = TextFieldDefaults.colors(
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedContainerColor = Theme.color.container.surface.copy(.2f),
        errorContainerColor = Theme.color.container.error.copy(.2f),
        disabledContainerColor = Color.Transparent
    ),
    keyboardOptions = keyboardOptions,
    keyboardActions = keyboardActions,
    leadingIcon = leadingIcon,
    visualTransformation = visualTransformation
)

@Preview
@Composable
private fun SettingsTextFieldPreview() = PreviewLayout {
    SettingsTextField(
        value = "",
        onValueChange = {},
        leadingIcon = { Icon(Icons.Default.LocationOn, null) }
    )
}