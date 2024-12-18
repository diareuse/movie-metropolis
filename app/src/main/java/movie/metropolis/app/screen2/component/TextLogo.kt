package movie.metropolis.app.screen2.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.PreviewLayout

@Composable
fun TextLogo(
    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
) {
    /*Text(
        "MOVIE",
        fontWeight = FontWeight.Black,
        fontSize = 16.sp,
        lineHeight = 16.sp,
        maxLines = 1,
        letterSpacing = 10.5.sp,
        modifier = Modifier.offset(y = 3.dp)
    )
    Text(
        "METROPOLIS",
        fontWeight = FontWeight.Light,
        fontSize = 16.sp,
        lineHeight = 16.sp,
        maxLines = 1,
        letterSpacing = 0.sp,
        modifier = Modifier.offset(y = (-3).dp)
    )*/
    Text(
        "MOVIE",
        fontWeight = FontWeight.Black,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        maxLines = 1,
        letterSpacing = 0.sp,
        modifier = Modifier.offset(y = 3.dp)
    )
    Text(
        "METROPOLIS",
        fontWeight = FontWeight.Light,
        fontSize = 13.sp,
        lineHeight = 13.sp,
        maxLines = 1,
        letterSpacing = 0.sp,
        modifier = Modifier.offset(y = (-3).dp)
    )
}

@PreviewFontScale
@Composable
private fun TextLogoPreview() = PreviewLayout {
    TextLogo(Modifier.background(Color.White))
}