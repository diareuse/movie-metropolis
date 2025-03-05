package movie.metropolis.app.ui.movie.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import movie.style.layout.PreviewLayout
import movie.style.util.pc

private val Gold = Brush.linearGradient(
    0f to Color(0xffebd197),
    .5f to Color(0xffb48811),
    .51f to Color(0xffa2790d),
    1f to Color(0xffbb9b49)
)
private val Silver = Brush.linearGradient(
    0f to Color(0xffDADADA),
    .5f to Color(0xff8D8D8D),
    .51f to Color(0xff737373),
    1f to Color(0xffC0C0C0)
)
private val Bronze = Brush.linearGradient(
    0f to Color(0xffD7995B),
    .5f to Color(0xff7B4C1E),
    .51f to Color(0xff523314),
    1f to Color(0xffA46628)
)
private val Shit = Brush.linearGradient(
    0f to Color(0xffFF8C00),
    .5f to Color(0xffE70000),
    .51f to Color(0xff760089),
    1f to Color(0xff0044FF)
)

@Composable
fun RatingText(
    text: String,
    progress: Float,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier
            .clip(CircleShape)
            .drawWithCache {
                val brush = when {
                    progress >= .8f -> Gold
                    progress >= .65f -> Silver
                    progress >= .5f -> Bronze
                    else -> Shit
                }
                onDrawWithContent {
                    drawRect(brush)
                    drawContent()
                }
            }
            .padding(1.pc, .25.pc),
        text = text,
        color = Color.White,
        fontWeight = FontWeight.Black
    )
}

@PreviewLightDark
@Composable
private fun RatingTextPreview() = PreviewLayout {
    Surface {
        Column {
            RatingText("10%", .1f)
            RatingText("50%", .5f)
            RatingText("65%", .65f)
            RatingText("80%", .8f)
        }
    }
}