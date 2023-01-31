@file:OptIn(ExperimentalMaterial3Api::class)

package movie.style

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolbar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    CenterAlignedTopAppBar(
        modifier = modifier
            .applyScrollBehavior(scrollBehavior)
            .padding(horizontal = 8.dp),
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        )
    )
}

@JvmName("applyScrollBehaviorNullable")
fun Modifier.applyScrollBehavior(scrollBehavior: TopAppBarScrollBehavior?) =
    if (scrollBehavior == null) this
    else applyScrollBehavior(scrollBehavior)

fun Modifier.applyScrollBehavior(scrollBehavior: TopAppBarScrollBehavior) = composed {
    val offset = scrollBehavior.state.heightOffset
    var heightOffsetLimit by remember { mutableStateOf(0f) }
    LaunchedEffect(heightOffsetLimit) {
        if (scrollBehavior.state.heightOffsetLimit != heightOffsetLimit) {
            scrollBehavior.state.heightOffsetLimit = heightOffsetLimit
        }
    }
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints).apply {
            heightOffsetLimit = -height.toFloat()
        }
        val width = placeable.width
        val height = placeable.height - offset.toInt().absoluteValue
        layout(width, height) {
            placeable.place(0, height - placeable.height)
        }
    }
}