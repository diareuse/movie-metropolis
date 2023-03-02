package movie.metropolis.app.screen.profile.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.*
import movie.metropolis.app.R
import movie.style.AppIconButton
import movie.style.InputField
import movie.style.layout.PreviewLayout

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    supportingText: (@Composable () -> Unit)? = null,
    readOnly: Boolean = false,
    error: Boolean = false
) {
    var isHidden by remember { mutableStateOf(true) }
    InputField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isHidden) KeyboardType.Password else KeyboardType.Text
        ),
        isError = error,
        readOnly = readOnly,
        placeholder = { Text("p4$\$w0rd") },
        label = { Text(stringResource(R.string.password)) },
        supportingText = supportingText,
        trailingIcon = {
            AppIconButton(
                painter = painterResource(R.drawable.ic_eye),
                onClick = { isHidden = !isHidden }
            )
        }
    )
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun PasswordFieldPreview() = PreviewLayout {
    PasswordField(value = "", onValueChange = {})
}