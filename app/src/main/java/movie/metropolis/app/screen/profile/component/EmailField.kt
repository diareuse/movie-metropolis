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
import movie.style.InputField
import movie.style.layout.PreviewLayout

@Composable
fun EmailField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    supportingText: (@Composable () -> Unit)? = null,
    readOnly: Boolean = false,
    error: Boolean = false,
    imeAction: ImeAction = ImeAction.Done,
    onFocusChangeRequest: () -> Unit = {}
) {
    InputField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = imeAction),
        keyboardActions = KeyboardActions(onNext = { onFocusChangeRequest() }),
        isError = error,
        readOnly = readOnly,
        placeholder = { Text("john.doe@cinema.com") },
        label = { Text(stringResource(R.string.email)) },
        supportingText = supportingText
    )
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun EmailFieldPreview() = PreviewLayout {
    EmailField(value = "", onValueChange = {})
}