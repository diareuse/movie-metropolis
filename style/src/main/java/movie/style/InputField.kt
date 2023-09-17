package movie.style

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    readOnly: Boolean = false,
    placeholder: @Composable () -> Unit = {},
    label: (@Composable () -> Unit)? = null,
    supportingText: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Theme.color.container.secondary.copy(alpha = .2f),
        contentColor = Theme.color.content.background,
        shape = Theme.container.button
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = if (supportingText != null) 4.dp else 0.dp)
        ) {
            if (label != null) Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                label()
            }
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = value,
                onValueChange = onValueChange,
                keyboardOptions = keyboardOptions,
                isError = isError,
                readOnly = readOnly,
                shape = Theme.container.button,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Theme.color.content.surface,
                    unfocusedTextColor = Theme.color.content.surface,
                    disabledTextColor = Theme.color.content.surface.copy(alpha = .38f),
                    focusedContainerColor = Theme.color.container.surface,
                    unfocusedContainerColor = Theme.color.container.surface,
                    disabledContainerColor = Theme.color.container.surface,
                    cursorColor = Theme.color.content.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    focusedPlaceholderColor = Theme.color.content.surface.copy(alpha = .5f),
                    unfocusedPlaceholderColor = Theme.color.content.surface.copy(alpha = .5f),
                    disabledPlaceholderColor = Theme.color.content.surface.copy(alpha = .25f),
                ),
                placeholder = placeholder,
                supportingText = supportingText,
                visualTransformation = when (keyboardOptions.keyboardType) {
                    KeyboardType.Password,
                    KeyboardType.NumberPassword -> PasswordVisualTransformation()

                    else -> VisualTransformation.None
                },
                trailingIcon = trailingIcon
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> InputField(
    selected: T,
    items: List<T>,
    converter: (T) -> String,
    modifier: Modifier = Modifier,
    label: (@Composable () -> Unit)? = null,
    content: @Composable (T) -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = modifier.fillMaxWidth(),
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        InputField(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .menuAnchor(),
            value = converter(selected),
            label = label,
            onValueChange = {},
            readOnly = true,
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            for (item in items) content(item)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        InputField(
            value = "",
            onValueChange = {},
            modifier = Modifier.padding(24.dp),
            label = { Text("Email") },
            placeholder = { Text("john.doe@email.com") }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview2() {
    Theme {
        InputField(
            selected = "foo",
            items = listOf("foo", "bar"),
            converter = { it },
            modifier = Modifier.padding(24.dp),
            label = { Text("items") }
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview3() {
    Theme {
        InputField(
            value = "",
            onValueChange = {},
            modifier = Modifier.padding(24.dp),
            label = { Text("Email") },
            placeholder = { Text("john.doe@email.com") },
            supportingText = { Text("Foo bar") }
        )
    }
}