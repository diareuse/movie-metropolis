@file:OptIn(ExperimentalMaterial3Api::class)

package movie.metropolis.app.screen2.booking.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.PreviewLayout
import movie.style.theme.Theme

@Composable
fun FilterBox(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Theme.color.container.surface,
    contentColor: Color = Theme.color.content.surface,
    content: @Composable () -> Unit,
) {
    val leadingIcon: (@Composable () -> Unit)? = if (selected) (@Composable {
        Icon(Icons.Rounded.Check, null)
    }) else null
    FilterChip(
        modifier = modifier,
        selected = selected,
        onClick = onClick,
        label = content,
        leadingIcon = leadingIcon,
        shape = CircleShape,
        colors = FilterChipDefaults.filterChipColors(
            iconColor = contentColor,
            containerColor = Color.Transparent,
            labelColor = contentColor,
            selectedContainerColor = contentColor,
            selectedLabelColor = color,
            selectedLeadingIconColor = color,
            selectedTrailingIconColor = color
        ),
        border = null
    )
}

@Preview(showBackground = true)
@Composable
private fun FilterChipPreview(
    @PreviewParameter(BooleanParameter::class)
    selected: Boolean
) = PreviewLayout(modifier = Modifier.padding(16.dp)) {
    FilterBox(selected, {}, color = Color(0xFFFF6E40), contentColor = Color.Black) {
        ProjectionTypeRowDefaults.Type4DX()
    }
}

class BooleanParameter : PreviewParameterProvider<Boolean> {
    override val values = sequence {
        yield(true)
        yield(false)
    }
}