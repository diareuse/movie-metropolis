package movie.metropolis.app.screen.detail.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.screen.detail.component.ShowingItemTimeParameter.Data
import movie.style.AppButton
import movie.style.layout.PreviewLayout
import movie.style.theme.Theme

@Composable
fun ShowingItemTime(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    time: @Composable () -> Unit
) {
    AppButton(
        modifier = modifier,
        onClick = onClick,
        elevation = 0.dp,
        containerColor = Theme.color.container.secondary,
        contentColor = Theme.color.content.secondary,
        contentPadding = PaddingValues(16.dp, 8.dp)
    ) {
        ProvideTextStyle(Theme.textStyle.body.copy(fontWeight = FontWeight.Bold)) {
            time()
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun ShowingItemTimePreview(
    @PreviewParameter(ShowingItemTimeParameter::class)
    parameter: Data
) = PreviewLayout {
    ShowingItemTime {
        Text(parameter.time)
    }
}

private class ShowingItemTimeParameter :
    CollectionPreviewParameterProvider<Data>(listOf(Data())) {
    data class Data(val time: String = "12:45")
}