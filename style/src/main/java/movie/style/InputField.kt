package movie.style

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import movie.style.theme.Theme

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
    supportingText: String? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Theme.color.container.background,
        contentColor = Theme.color.content.background,
        shape = Theme.container.button,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = if (supportingText != null) 4.dp else 0.dp)
        ) {
            if (label != null) Text(
                text = label,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = value,
                onValueChange = onValueChange,
                keyboardOptions = keyboardOptions,
                isError = isError,
                readOnly = readOnly,
                shape = Theme.container.button,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Theme.color.content.surface,
                    disabledTextColor = Theme.color.content.surface.copy(alpha = .38f),
                    containerColor = Theme.color.container.surface,
                    cursorColor = Theme.color.content.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    placeholderColor = Theme.color.content.surface.copy(alpha = .5f),
                    disabledPlaceholderColor = Theme.color.content.surface.copy(alpha = .25f),
                ),
                placeholder = if (placeholder == null) null else (@Composable { Text(placeholder) }),
                supportingText = if (supportingText == null) null else (@Composable {
                    Text(
                        supportingText
                    )
                }),
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
    label: String? = null,
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
            label = "Email",
            placeholder = "john.doe@email.com"
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
            label = "items"
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
            label = "Email",
            placeholder = "john.doe@email.com",
            supportingText = "Foo bar"
        )
    }
}