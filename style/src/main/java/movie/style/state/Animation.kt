package movie.style.state

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*

@Composable
fun Float.animate() = animateFloatAsState(targetValue = this).value

@Composable
fun Color.animate() = animateColorAsState(targetValue = this).value

@Composable
fun Dp.animate() = animateDpAsState(targetValue = this).value