package movie.metropolis.app.screen.setup.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.DefaultPosterAspectRatio
import movie.style.layout.PreviewLayout
import kotlin.math.absoluteValue

@Composable
fun SetupPreviewLayout(
    count: Int,
    selectedItem: Int,
    state: LazyStaggeredGridState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    rowCount: Int = 3,
    content: @Composable (Int) -> Unit,
) {
    LaunchedEffect(state) {
        val width = state.layoutInfo.viewportSize.width.toFloat()
        var direction = 1
        while (true) {
            val consumed = state.animateScrollBy(width * direction, tween(5000))
            if (consumed.absoluteValue != width) {
                direction *= -1
            }
        }
    }
    LazyHorizontalStaggeredGrid(
        modifier = modifier,
        state = state,
        rows = StaggeredGridCells.Fixed(rowCount),
        userScrollEnabled = false,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalItemSpacing = 8.dp,
        contentPadding = contentPadding
    ) {
        repeat(rowCount - 1) {
            item {
                Box(
                    modifier = Modifier
                        .width(40.dp / it)
                        .height(1.dp)
                )
            }
        }
        items(count) {
            val zIndex by animateFloatAsState(if (it == selectedItem) 5f else 0f, tween(700))
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .zIndex(zIndex)
                    .aspectRatio(DefaultPosterAspectRatio, true),
                propagateMinConstraints = true
            ) {
                content(it)
            }
        }
    }
}

@Preview
@Composable
private fun SetupPreviewLayoutPreview(
    @PreviewParameter(SetupPreviewLayoutParameter::class)
    parameter: SetupPreviewLayoutParameter.Data
) = PreviewLayout {
    SetupPreviewLayout(
        modifier = Modifier,
        count = 20,
        state = rememberLazyStaggeredGridState(),
        selectedItem = 0
    ) {
        Box(Modifier.background(Color.Blue))
    }
}

private class SetupPreviewLayoutParameter :
    PreviewParameterProvider<SetupPreviewLayoutParameter.Data> {
    override val values = sequence { yield(Data()) }

    class Data
}