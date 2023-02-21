package movie.metropolis.app.screen.profile.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import com.google.zxing.BarcodeFormat
import movie.metropolis.app.R
import movie.style.Barcode
import movie.style.imagePlaceholder
import movie.style.layout.StackedCardLayout
import movie.style.textPlaceholder
import movie.style.theme.Theme
import movie.style.theme.extendBy

@Composable
fun MembershipCard(
    firstName: String,
    lastName: String,
    cardNumber: String,
    until: String,
    points: String,
    modifier: Modifier = Modifier,
) {
    MembershipCardLayout(
        modifier = modifier,
        name = { Text("%s %s".format(firstName, lastName)) },
        expiration = { Text(stringResource(R.string.expires_at, until)) },
        points = { Text(stringResource(R.string.points_count, points)) },
        barcode = {
            Barcode(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(Theme.container.poster.extendBy(padding = 8.dp))
                    .background(Color.White)
                    .height(64.dp)
                    .padding(vertical = 8.dp),
                code = cardNumber,
                format = BarcodeFormat.CODE_128,
                color = Color.Black
            )
        }
    )
}

@Composable
fun MembershipCard(modifier: Modifier = Modifier) {
    MembershipCardLayout(
        modifier = modifier,
        name = { Text("#".repeat(22), modifier = Modifier.textPlaceholder(true)) },
        expiration = { Text("#".repeat(11), modifier = Modifier.textPlaceholder(true)) },
        points = { Text("#".repeat(10), modifier = Modifier.textPlaceholder(true)) },
        barcode = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(Theme.container.poster.extendBy(padding = 8.dp))
                    .height(64.dp)
                    .imagePlaceholder(true)
            )
        }
    )
}

@Composable
private fun MembershipCardLayout(
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
            barcode()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        MembershipCard(
            firstName = "Jonathan",
            lastName = "SuperLongNamer",
            cardNumber = "1234612377231",
            until = "21.5.2053",
            points = "513.2"
        )
    }
}
