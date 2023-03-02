package movie.metropolis.app.screen.home.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import movie.metropolis.app.R
import movie.metropolis.app.screen.home.component.SelectableNavigationBarItemParameter.Data
import movie.style.layout.PreviewLayout

@Composable
fun <T> RowScope.SelectableNavigationBarItem(
    selected: T,
    index: T,
    icon: Int,
    label: String,
    onSelected: (T) -> Unit,
) {
    NavigationBarItem(
        selected = selected == index,
        onClick = { onSelected(index) },
        colors = NavigationBarItemDefaults.colors(),
        icon = {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null
            )
        },
        label = { Text(label) }
    )
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun SelectableNavigationBarItemPreview(
    @PreviewParameter(SelectableNavigationBarItemParameter::class)
    parameter: Data
) = PreviewLayout {
    Row {
        SelectableNavigationBarItem(
            selected = parameter.selected,
            index = parameter.value,
            icon = parameter.icon,
            label = parameter.value,
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