package movie.metropolis.app.screen.profile.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import androidx.compose.ui.unit.*
import com.google.zxing.BarcodeFormat
import movie.metropolis.app.R
import movie.metropolis.app.screen.profile.component.MembershipCardLayoutParameter.Data
import movie.style.Barcode
import movie.style.layout.PreviewLayout
import movie.style.layout.StackedCardLayout
import movie.style.modifier.surface
import movie.style.theme.Theme
import movie.style.theme.extendBy

@Composable
fun MembershipCardLayout(
    name: @Composable () -> Unit,
    expiration: @Composable () -> Unit,
    points: @Composable () -> Unit,
    barcode: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    StackedCardLayout(
        modifier = modifier,
        headline = { Text(stringResource(R.string.club_membership)) },
        color = Theme.color.container.primary,
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(Modifier.padding(horizontal = 16.dp)) {
                CompositionLocalProvider(LocalTextStyle provides Theme.textStyle.title) {
                    name()
                }
                expiration()
                points()
            }
            Spacer(Modifier.height(16.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .surface(Color.White, Theme.container.poster.extendBy(padding = 8.dp))
                    .padding(vertical = 8.dp)
            ) {
                barcode()
            }
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun MembershipCardLayoutPreview(
    @PreviewParameter(MembershipCardLayoutParameter::class)
    parameter: Data
) = PreviewLayout {
    MembershipCardLayout(
        name = { Text(parameter.name) },
        expiration = { Text(parameter.expiration) },
        points = { Text(parameter.points) },
        barcode = {
            Barcode(
                parameter.barcode,
                format = BarcodeFormat.CODE_128,
                modifier = Modifier.fillMaxSize()
            )
        }
    )
}

private class MembershipCardLayoutParameter :
    CollectionPreviewParameterProvider<Data>(listOf(Data())) {
    data class Data(
        val name: String = "Name Surname",
        val expiration: String = "Jan 1 2024",
        val points: String = "100 points",
        val barcode: String = "0123456789"
    )
}