package movie.style

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.ripple.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import androidx.compose.ui.unit.*
import movie.style.AppNavigationBarItemParameter.Data
import movie.style.layout.PreviewLayout
import movie.style.state.animate
import movie.style.theme.Theme
import movie.style.theme.contentColorFor

@Composable
fun <T> AppNavigationBarItem(
    selected: T,
    index: T,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    size: WindowSizeClass = LocalWindowSizeClass.current,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    when (size.widthSizeClass) {
        WindowWidthSizeClass.Compact -> AppNavigationBarItemCompact(
            selected,
            index,
            icon,
            label,
            onSelected,
            modifier,
            interactionSource
        )

        WindowWidthSizeClass.Medium -> AppNavigationBarItemMedium(
            selected,
            index,
            icon,
            label,
            onSelected,
            modifier,
            interactionSource
        )

        WindowWidthSizeClass.Expanded -> AppNavigationBarItemExtended(
            selected,
            index,
            icon,
            label,
            onSelected,
            modifier,
            interactionSource
        )
    }
}

@Composable
private fun <T> AppNavigationBarItemCompact(
    selected: T,
    index: T,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val color by when (selected) {
            index -> Theme.color.container.primary
            else -> Theme.color.container.background
        }.animate()
        val contentColor = Theme.color.contentColorFor(color)
        Surface(
            modifier = modifier,
            shape = CircleShape,
            color = color,
            contentColor = contentColor,
            onClick = { onSelected(index) }
        ) {
            Row(
                modifier = Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(false),
                    onClick = { onSelected(index) }
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(12.dp),
                    propagateMinConstraints = true
                ) {
                    icon()
                }
                ProvideTextStyle(Theme.textStyle.body.copy(fontWeight = FontWeight.Bold)) {
                    AnimatedVisibility(selected == index) {
                        Box(modifier = Modifier.padding(end = 16.dp)) {
                            label()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun <T> AppNavigationBarItemMedium(
    selected: T,
    index: T,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val color by when (selected) {
            index -> Theme.color.container.primary
            else -> Theme.color.container.background
        }.animate()
        val contentColor = Theme.color.contentColorFor(color)
        Surface(
            modifier = modifier,
            shape = Theme.container.button,
            color = color,
            contentColor = contentColor,
            onClick = { onSelected(index) }
        ) {
            Column(
                modifier = Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(false),
                    onClick = { onSelected(index) }
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(12.dp),
                    propagateMinConstraints = true
                ) {
                    icon()
                }
                ProvideTextStyle(Theme.textStyle.body.copy(fontWeight = FontWeight.Bold)) {
                    AnimatedVisibility(selected == index) {
                        Box(
                            modifier = Modifier
                                .width(64.dp)
                                .padding(bottom = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            label()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun <T> AppNavigationBarItemExtended(
    selected: T,
    index: T,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val color by when (selected) {
            index -> Theme.color.container.primary
            else -> Theme.color.container.background
        }.animate()
        val contentColor = Theme.color.contentColorFor(color)
        Surface(
            modifier = modifier,
            shape = Theme.container.button,
            color = color,
            contentColor = contentColor,
            onClick = { onSelected(index) }
        ) {
            Row(
                modifier = Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(false),
                    onClick = { onSelected(index) }
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(12.dp),
                    propagateMinConstraints = true
                ) {
                    icon()
                }
                ProvideTextStyle(Theme.textStyle.body.copy(fontWeight = FontWeight.Bold)) {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .width(150.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        label()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun AppNavigationBarItemPreview(
    @PreviewParameter(AppNavigationBarItemParameter::class)
    parameter: Data
) = PreviewLayout(padding = PaddingValues()) {
    AppNavigationBar(size = parameter.sizeClass) {
        AppNavigationBarItem(
            selected = parameter.selected,
            index = "This",
            label = { Text("This") },
            icon = { Icon(painterResource(parameter.icon), null) },
            onSelected = {},
            size = parameter.sizeClass
        )
        AppNavigationBarItem(
            selected = parameter.selected,
            index = "That",
            label = { Text("This") },
            icon = { Icon(painterResource(parameter.icon), null) },
            onSelected = {},
            size = parameter.sizeClass
        )
        AppNavigationBarItem(
            selected = parameter.selected,
            index = "Other",
            label = { Text("This") },
            icon = { Icon(painterResource(parameter.icon), null) },
            onSelected = {},
            size = parameter.sizeClass
        )
        AppNavigationBarItem(
            selected = parameter.selected,
            index = "Other",
            label = { Text("This") },
            icon = { Icon(painterResource(parameter.icon), null) },
            onSelected = {},
            size = parameter.sizeClass
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
private class AppNavigationBarItemParameter : CollectionPreviewParameterProvider<Data>(
    listOf(
        Data(value = "That", WindowSizeClass.calculateFromSize(DpSize(0.dp, 0.dp))),
        Data(value = "This", WindowSizeClass.calculateFromSize(DpSize(600.dp, 480.dp))),
        Data(value = "This", WindowSizeClass.calculateFromSize(DpSize(840.dp, 900.dp))),
    )
) {
    data class Data(
        val value: String,
        val sizeClass: WindowSizeClass,
        val selected: String = "This",
        val icon: Int = R.drawable.ic_image_error
    )
}