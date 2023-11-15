package movie.metropolis.app.screen.home.component

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.*
import movie.style.theme.Theme

object TransparentBottomNavigationItemDefaults {
    @Composable
    fun colors(
        active: Color = Theme.color.emphasis.primary,
        inactive: Color = Theme.color.content.background
    ) = TransparentBottomNavigationItemColors(
        active = active,
        inactive = inactive
    )
}