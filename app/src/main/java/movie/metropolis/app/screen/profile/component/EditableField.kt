package movie.metropolis.app.screen.profile.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.interaction.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import movie.style.layout.PreviewLayout

@Composable
fun EditableField(
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    TextField(
        modifier = modifier,
        value = "",
        onValueChange = {},
        interactionSource = interactionSource
    )
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun EditableFieldPreview(
    @PreviewParameter(EditableFieldParameter::class)
    parameter: EditableFieldParameter.Data
) = PreviewLayout {
    EditableField()
}

private class EditableFieldParameter : PreviewParameterProvider<EditableFieldParameter.Data> {
    override val values = sequence { yield(Data()) }

    class Data
}