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
    label: @Composable () -> Unit = { Text(stringResource(R.string.password)) },
    supportingText: (@Composable () -> Unit)? = null,
    readOnly: Boolean = false,
    error: Boolean = false,
    imeAction: ImeAction = ImeAction.Default,
    onClickDone: () -> Unit = {}
) {
    var isHidden by remember { mutableStateOf(true) }
    InputField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isHidden) KeyboardType.Password else KeyboardType.Text,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(onDone = { onClickDone() }),
        isError = error,
        readOnly = readOnly,
        placeholder = { Text("p4$\$w0rd") },
        label = label,
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