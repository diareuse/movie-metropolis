package movie.metropolis.app.screen2.setup

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import movie.style.layout.PreviewLayout

@Composable
fun SetupLoginContent(
    modifier: Modifier = Modifier,
) {

}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun SetupLoginContentPreview() = PreviewLayout {
    SetupLoginContent()
}

private class SetupLoginContentParameter :
    PreviewParameterProvider<SetupLoginContentParameter.Data> {
    override val values = sequence { yield(Data()) }

    class Data
}