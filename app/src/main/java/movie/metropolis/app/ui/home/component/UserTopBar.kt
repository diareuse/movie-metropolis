package movie.metropolis.app.ui.home.component

import android.os.Build
import android.view.RoundedCorner
import android.view.RoundedCorner.POSITION_BOTTOM_LEFT
import android.view.RoundedCorner.POSITION_BOTTOM_RIGHT
import android.view.RoundedCorner.POSITION_TOP_LEFT
import android.view.RoundedCorner.POSITION_TOP_RIGHT
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.util.*
import movie.metropolis.app.R
import movie.style.ContentPlaceholder
import movie.style.TextPlaceholder
import movie.style.layout.PreviewLayout
import movie.style.util.pc

@Composable
fun rememberDisplayShape(): Shape {
    val insets = LocalView.current.rootWindowInsets
    val ld = LocalLayoutDirection.current
    val shapes = MaterialTheme.shapes

    return remember(insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            fun RoundedCorner?.toCornerSize() = when {
                this == null -> CornerSize(0)
                else -> CornerSize(size = radius.toFloat())
            }

            val topLeft = insets.getRoundedCorner(POSITION_TOP_LEFT).toCornerSize()
            val topRight = insets.getRoundedCorner(POSITION_TOP_RIGHT).toCornerSize()
            val bottomLeft = insets.getRoundedCorner(POSITION_BOTTOM_LEFT).toCornerSize()
            val bottomRight = insets.getRoundedCorner(POSITION_BOTTOM_RIGHT).toCornerSize()
            RoundedCornerShape(
                topStart = if (ld == LayoutDirection.Ltr) topLeft else topRight,
                topEnd = if (ld == LayoutDirection.Ltr) topRight else topLeft,
                bottomStart = if (ld == LayoutDirection.Ltr) bottomLeft else bottomRight,
                bottomEnd = if (ld == LayoutDirection.Ltr) bottomRight else bottomLeft
            )
        } else {
            shapes.extraLarge
        }
    }
}

@Composable
fun UserTopBar(
    icon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    subtitle: @Composable () -> Unit,
    card: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.inverseSurface,
    ratio: Float = .7f
) = Layout(modifier = modifier.padding(1.pc), content = {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .background(LocalContentColor.current, rememberDisplayShape())
    )
    Column(
        modifier = Modifier
            .padding(1.pc)
            .statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(2.pc)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(1.pc),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.contentColorFor(color)
            ) {
                Box(
                    modifier = Modifier
                        .background(LocalContentColor.current, CircleShape)
                        .shadow(4.dp, CircleShape)
                        .clip(CircleShape)
                        .size(48.dp)
                        .border(2.dp, LocalContentColor.current, CircleShape),
                    propagateMinConstraints = true
                ) {
                    icon()
                }
                Column {
                    ProvideTextStyle(MaterialTheme.typography.titleMedium) {
                        title()
                    }
                    Box(modifier = Modifier.alpha(.5f)) {
                        ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                            subtitle()
                        }
                    }
                }
            }
        }
        card()
    }
}) { (backdrop, content), constraints ->
    val contentP = content.measure(constraints)
    val backdropP = backdrop.measure(
        Constraints.fixed(
            width = contentP.width,
            height = (contentP.height * ratio).fastRoundToInt()
        )
    )
    layout(contentP.width, contentP.height) {
        backdropP.place(0, 0)
        contentP.place(0, 0)
    }
}

@Composable
fun UserTopBar(modifier: Modifier = Modifier) {
    UserTopBar(
        icon = { ContentPlaceholder() },
        title = { TextPlaceholder(64.dp) },
        subtitle = { TextPlaceholder(48.dp) },
        card = {
            LoyaltyCardFront(
                modifier = Modifier.aspectRatio(3.37f / 2.125f),
                title = { TextPlaceholder(48.dp) },
                name = {
                    Row {
                        TextPlaceholder(32.dp)
                        Spacer(Modifier.width(.5.pc))
                        TextPlaceholder(48.dp)
                    }
                },
                expiration = { TextPlaceholder(48.dp) },
                number = { TextPlaceholder(200.dp) }
            )
        },
        modifier = modifier
    )
}

@PreviewLightDark
@PreviewFontScale
@Composable
private fun UserTopBarPreview() = PreviewLayout {
    UserTopBar(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        icon = { Box(modifier = Modifier.background(Color.Gray)) },
        title = { Text("Morning, Viktor.") },
        subtitle = { Text("Premium account") },
        card = {
            LoyaltyCardFront(
                modifier = Modifier
                    .aspectRatio(3.37f / 2.125f)
                    .fillMaxWidth(),
                logo = {
                    Icon(painterResource(R.drawable.ic_logo_cinemacity), null)
                },
                title = { Text("Premium") },
                name = { Text("John Doe") },
                number = { Text("1234 5678 9012") },
                expiration = { Text("01/02/24") }
            )
        }
    )
}

@PreviewLightDark
@Composable
private fun UserTopBarPlaceholderPreview() = PreviewLayout {
    UserTopBar(
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    )
}