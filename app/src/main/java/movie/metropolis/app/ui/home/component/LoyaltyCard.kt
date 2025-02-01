package movie.metropolis.app.ui.home.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.R
import movie.style.layout.PreviewLayout
import movie.style.util.pc

fun Float.squeezeInto(
    original: ClosedFloatingPointRange<Float>,
    range: ClosedFloatingPointRange<Float>
): Float {
    val progress = (this - original.start) / (original.endInclusive - original.start)
    return range.start + (range.endInclusive - range.start) * progress
}

@Composable
fun LoyaltyCard(
    title: @Composable () -> Unit,
    name: @Composable () -> Unit,
    expiration: @Composable () -> Unit,
    number: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    logo: @Composable () -> Unit = { Icon(painterResource(R.drawable.ic_logo_cinemacity), null) },
    shape: Shape = RoundedCornerShape(8)
) = Column(
    modifier = modifier
        .aspectRatio(3.37f / 2.125f)
        .shadow(
            elevation = 16.dp,
            shape = shape,
            spotColor = Color(0xFFF01FFF),
            ambientColor = Color(0xFF7700FF),
            clip = false
        )
        .clip(shape)
        .border(1.dp, Color.White.copy(.4f), shape)
        .drawWithCache {
            onDrawWithContent {
                val white = Color.White
                val transparent = Color.Transparent
                drawContent()
                // left bottom
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(white.copy(.25f), transparent),
                        center = Offset(0f, Float.POSITIVE_INFINITY),
                        radius = size.width / 2
                    ),
                    blendMode = BlendMode.Plus
                )
                // right bottom
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(white.copy(.25f), transparent),
                        center = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
                        radius = size.width / 2
                    ),
                    blendMode = BlendMode.Plus
                )
            }
        }
) {
    CompositionLocalProvider(LocalContentColor provides Color.White) {
        Column(
            modifier = Modifier
                .weight(1f)
                .drawWithCache {
                    onDrawWithContent {
                        val red = Color(0xFFFF0800)
                        val pink = Color(0xFFF01FFF)
                        val purple = Color(0xFF7700FF)
                        val white = Color.White
                        val transparent = Color.Transparent
                        drawRect(red)
                        drawRect(
                            Brush.radialGradient(
                                colors = listOf(pink, pink.copy(0f)),
                                center = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
                                radius = size.maxDimension
                            )
                        )
                        drawRect(
                            Brush.radialGradient(
                                colors = listOf(purple, purple.copy(0f)),
                                center = Offset(size.width / 2, Float.POSITIVE_INFINITY),
                                radius = size.maxDimension / 1.5f
                            )
                        )
                        drawContent()
                        // --- highlights
                        drawRect(
                            brush = Brush.linearGradient(
                                colors = listOf(white.copy(.2f), transparent),
                                start = Offset(0f, Float.POSITIVE_INFINITY),
                                end = Offset(0f, size.height - 2.pc.toPx())
                            ),
                            blendMode = BlendMode.Plus
                        )
                        drawRect(
                            brush = Brush.linearGradient(
                                colors = listOf(white.copy(.3f), transparent),
                                end = Offset(5.pc.toPx(), 5.pc.toPx())
                            ),
                            blendMode = BlendMode.Plus
                        )
                        drawRect(
                            brush = Brush.linearGradient(
                                colors = listOf(white.copy(.3f), transparent),
                                start = Offset(Float.POSITIVE_INFINITY, 0f),
                                end = Offset(size.width - 5.pc.toPx(), 5.pc.toPx())
                            ),
                            blendMode = BlendMode.Plus
                        )
                        drawRect(
                            brush = Brush.radialGradient(
                                colors = listOf(white.copy(.25f), transparent),
                                center = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
                                radius = size.width / 2
                            ),
                            blendMode = BlendMode.Plus
                        )
                    }
                }
                .padding(2.pc)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                title()
                logo()
            }
            Spacer(Modifier.weight(1f))
            ProvideTextStyle(MaterialTheme.typography.titleLarge.copy(fontFamily = FontFamily.Monospace)) {
                number()
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(2.pc),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            name()
            Row {
                Text("Exp ")
                expiration()
            }
        }
    }
}

@PreviewFontScale
@Composable
private fun LoyaltyCardPreview() = PreviewLayout {
    LoyaltyCard(
        modifier = Modifier
            .padding(1.pc)
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