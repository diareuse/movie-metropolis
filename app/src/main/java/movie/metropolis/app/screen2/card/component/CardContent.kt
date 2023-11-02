package movie.metropolis.app.screen2.card.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.PreviewLayout
import movie.style.theme.Theme

@Composable
fun CardContentFront(
    logo: @Composable () -> Unit,
    name: @Composable () -> Unit,
    cardholder: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    background: @Composable () -> Unit = { CardBackgroundFront() },
) = Box(modifier = modifier) {
    Box(
        modifier = Modifier.matchParentSize(),
        propagateMinConstraints = true
    ) {
        background()
    }
    Box(
        modifier = Modifier
            .padding(16.dp)
            .align(Alignment.TopEnd)
            .height(96.dp),
        propagateMinConstraints = true
    ) {
        logo()
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter),
        verticalAlignment = Alignment.Bottom
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            ProvideTextStyle(Theme.textStyle.body) {
                cardholder()
            }
        }
        Spacer(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .weight(1f)
        )
        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            ProvideTextStyle(Theme.textStyle.title.copy(fontSize = 32.sp)) {
                name()
            }
        }
    }
}

@Composable
fun CardContentBack(
    logo: @Composable () -> Unit,
    name: @Composable () -> Unit,
    code: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    background: @Composable () -> Unit = { CardBackgroundBack() },
) = Box(modifier = modifier) {
    Box(
        modifier = Modifier.matchParentSize(),
        propagateMinConstraints = true
    ) {
        background()
    }
    Box(
        modifier = Modifier
            .padding(16.dp)
            .align(Alignment.TopEnd)
            .height(48.dp),
        propagateMinConstraints = true
    ) {
        logo()
    }
    Box(
        modifier = Modifier
            .padding(16.dp)
            .align(Alignment.TopStart)
    ) {
        ProvideTextStyle(Theme.textStyle.title.copy(fontSize = 32.sp)) {
            name()
        }
    }
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(64.dp)
            .align(Alignment.BottomCenter),
        propagateMinConstraints = true
    ) {
        code()
    }
}

@Preview
@Composable
private fun CardContentFrontPreview() = PreviewLayout {
    CardContentFront(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.5f),
        background = { CardBackgroundFront() },
        logo = {
            Box(
                modifier = Modifier
                    .aspectRatio(1.5f)
                    .background(Color.Yellow)
            )
        },
        name = { Text("Club".uppercase()) },
        cardholder = {
            Text("10 points")
            Text("Name Surname")
        }
    )
}

@Preview
@Composable
private fun CardContentBackPreview() = PreviewLayout {
    CardContentBack(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.5f),
        background = { CardBackgroundBack() },
        logo = {
            Box(
                modifier = Modifier
                    .aspectRatio(1.5f)
                    .background(Color.Yellow)
            )
        },
        name = { Text("Club".uppercase()) },
        code = { Box(Modifier.background(Color.White)) }
    )
}