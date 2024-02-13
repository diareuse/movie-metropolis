package movie.metropolis.app.screen.home.component

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.animation.AnticipateOvershootEasing
import movie.style.layout.PreviewLayout

private const val AnimationDuration = 500

@Composable
fun TransparentBottomNavigationItem(
    selected: Boolean,
    active: @Composable () -> Unit,
    inactive: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: TransparentBottomNavigationItemColors = TransparentBottomNavigationItemDefaults.colors()
) {
    Box(
        modifier = modifier
            .clickable(
                role = Role.Tab,
                onClick = onClick,
                enabled = enabled
            )
            .minimumInteractiveComponentSize(),
        contentAlignment = Alignment.Center
    ) {
        val scale by animateFloatAsState(
            if (selected) 1.5f else 1f,
            tween(easing = AnticipateOvershootEasing, durationMillis = AnimationDuration)
        )
        val alpha by animateFloatAsState(if (selected) 1f else .7f, tween(AnimationDuration))
        Box(
            modifier = Modifier
                .size(24.dp)
                .scale(scale)
                .alpha(alpha),
            propagateMinConstraints = true
        ) {
            val currentContentColor by animateColorAsState(
                targetValue = when (selected) {
                    true -> colors.active
                    else -> colors.inactive
                },
                tween(AnimationDuration)
            )
            CompositionLocalProvider(LocalContentColor provides currentContentColor) {
                val content = when (selected) {
                    true -> active
                    else -> inactive
                }
                AnimatedContent(content, transitionSpec = { fadeIn() togetherWith fadeOut() }) {
                    it()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TransparentBottomNavigationItemPreview(
    @PreviewParameter(TransparentBottomNavigationItemParameter::class)
    selected: Boolean
) = PreviewLayout {
    TransparentBottomNavigationItem(
        selected = selected,
        active = { Icon(Icons.Filled.Home, null) },
        inactive = { Icon(Icons.Outlined.Home, null) },
        onClick = {}
    )
}

class TransparentBottomNavigationItemParameter : PreviewParameterProvider<Boolean> {
    override val values = sequence {
        yield(true)
        yield(false)
    }
}