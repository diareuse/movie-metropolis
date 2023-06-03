package movie.metropolis.app.screen.home.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.ripple.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.R
import movie.metropolis.app.screen.home.component.SelectableNavigationBarItemParameter.Data
import movie.style.layout.PreviewLayout
import movie.style.state.animate
import movie.style.theme.Theme
import movie.style.theme.contentColorFor

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier
            .navigationBarsPadding()
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp),
        shape = CircleShape,
        tonalElevation = 2.dp,
        shadowElevation = 16.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            content()
        }
    }
}

@Composable
fun <T> SelectableNavigationBarItem(
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

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun SelectableNavigationBarItemPreview(
    @PreviewParameter(SelectableNavigationBarItemParameter::class)
    parameter: Data
) = PreviewLayout(padding = PaddingValues()) {
    BottomNavigationBar {
        SelectableNavigationBarItem(
            selected = parameter.selected,
            index = "This",
            label = { Text("This") },
            icon = { Icon(painterResource(parameter.icon), null) },
            onSelected = {}
        )
        SelectableNavigationBarItem(
            selected = parameter.selected,
            index = "That",
            label = { Text("This") },
            icon = { Icon(painterResource(parameter.icon), null) },
            onSelected = {}
        )
        SelectableNavigationBarItem(
            selected = parameter.selected,
            index = "Other",
            label = { Text("This") },
            icon = { Icon(painterResource(parameter.icon), null) },
            onSelected = {}
        )
        SelectableNavigationBarItem(
            selected = parameter.selected,
            index = "Other",
            label = { Text("This") },
            icon = { Icon(painterResource(parameter.icon), null) },
            onSelected = {}
        )
    }
}

private class SelectableNavigationBarItemParameter : CollectionPreviewParameterProvider<Data>(
    listOf(
        Data(value = "That"),
        Data(value = "This")
    )
) {
    data class Data(
        val value: String,
        val selected: String = "This",
        val icon: Int = R.drawable.ic_popcorn
    )
}