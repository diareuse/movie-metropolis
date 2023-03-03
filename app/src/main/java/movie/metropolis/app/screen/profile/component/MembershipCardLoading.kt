package movie.metropolis.app.screen.profile.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.imagePlaceholder
import movie.style.layout.PreviewLayout
import movie.style.textPlaceholder
import movie.style.theme.Theme
import movie.style.theme.extendBy

@Composable
fun MembershipCardLoading(
    modifier: Modifier = Modifier,
) {
    MembershipCardLayout(
        modifier = modifier,
        name = { Text("#".repeat(22), modifier = Modifier.textPlaceholder()) },
        expiration = { Text("#".repeat(11), modifier = Modifier.textPlaceholder()) },
        points = { Text("#".repeat(10), modifier = Modifier.textPlaceholder()) },
        barcode = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(Theme.container.poster.extendBy(padding = 8.dp))
                    .height(64.dp)
                    .imagePlaceholder()
            )
        }
    )
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun MembershipCardLoadingPreview() = PreviewLayout {
    MembershipCardLoading()
}
