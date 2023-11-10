package movie.style

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.nestedscroll.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.PreviewLayout
import movie.style.layout.rememberWindowSizeClass

@Composable
fun TwoPaneSurface(
    primary: @Composable ColumnScope.() -> Unit,
    secondary: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    connection: NestedScrollConnection? = null,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    primaryWeight: Float = 1f,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) = when (rememberWindowSizeClass().widthSizeClass) {
    WindowWidthSizeClass.Compact -> Column(
        modifier = modifier
            .run { if (connection != null) nestedScroll(connection) else this }
            .verticalScroll(rememberScrollState())
            .padding(contentPadding),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        primary()
        secondary()
    }

    else -> Row(
        modifier = modifier,
        verticalAlignment = verticalAlignment,
        horizontalArrangement = horizontalArrangement
    ) {
        Column(
            modifier = Modifier
                .weight(primaryWeight)
                .verticalScroll(rememberScrollState())
                .padding(contentPadding),
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment
        ) {
            primary()
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .run { if (connection != null) nestedScroll(connection) else this }
                .verticalScroll(rememberScrollState())
                .padding(contentPadding),
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment
        ) {
            secondary()
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, widthDp = 1000)
@Composable
private fun TwoPaneSurfacePreview() = PreviewLayout {
    TwoPaneSurface(
        primaryWeight = .5f,
        primary = {
            Box(
                Modifier
                    .aspectRatio(0.67f, false)
                    .background(Brush.radialGradient(listOf(Color.Blue, Color.Red)))
            )
        },
        secondary = {
            Text("B", modifier = Modifier.fillMaxWidth())
        }
    )
}