package movie.metropolis.app.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    readOnly: Boolean = false,
    placeholder: String? = null,
    label: String? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        isError = isError,
        shape = MaterialTheme.shapes.medium.copy(
            bottomStart = CornerSize(0),
            bottomEnd = CornerSize(0)
        ),
        readOnly = readOnly,
        placeholder = if (placeholder == null) null else (@Composable { Text(placeholder) }),
        label = if (label == null) null else (@Composable { Text(label) }),
        visualTransformation = when (keyboardOptions.keyboardType) {
            KeyboardType.Password,
            KeyboardType.NumberPassword -> PasswordVisualTransformation()

            else -> VisualTransformation.None
        },
        trailingIcon = trailingIcon
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> InputField(
    selected: T,
    items: List<T>,
    converter: (T) -> String,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = modifier.fillMaxWidth(),
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = converter(selected),
            onValueChange = {},
            readOnly = true,
            shape = MaterialTheme.shapes.medium.copy(
                bottomStart = CornerSize(0),
                bottomEnd = CornerSize(0)
            )
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            for (item in items) content(item)
        }
    }
}