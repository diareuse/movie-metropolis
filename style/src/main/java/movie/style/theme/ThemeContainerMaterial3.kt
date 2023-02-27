package movie.style.theme

import androidx.compose.foundation.shape.*
import androidx.compose.material3.*

internal class ThemeContainerMaterial3(
    private val shapes: Shapes
) : Theme.Container {

    override val button: CornerBasedShape
        get() = shapes.medium
    override val buttonSmall: CornerBasedShape
        get() = shapes.small
    override val card: CornerBasedShape
        get() = shapes.large

}