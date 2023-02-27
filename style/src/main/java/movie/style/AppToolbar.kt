@file:OptIn(ExperimentalMaterial3Api::class)

package movie.style

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.*
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