package movie.style.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun Theme(content: @Composable () -> Unit) = MaterialTheme(
    colorScheme = themeColors(),
    typography = ThemeTypography,
    shapes = ThemeShapes
) {
    Surface {
        content()
    }
}

interface Theme {

    val textStyle: Text
    val container: Container
    val color: Style

    interface Text {
        val caption: TextStyle
        val body: TextStyle
        val emphasis: TextStyle
        val title: TextStyle
        val headline: TextStyle
    }

    interface Container {
        val button: CornerBasedShape
        val buttonSmall: CornerBasedShape
        val card: CornerBasedShape
    }

    interface Style {

        val container: Scheme
        val content: Scheme

        interface Scheme {
            val primary: Color
            val secondary: Color
            val tertiary: Color
            val error: Color
            val surface: Color
            val background: Color
            val outline: Color
        }

    }

    companion object {

        @Composable
        operator fun invoke(): Theme = ThemeMaterial3(
            typography = MaterialTheme.typography,
            shapes = MaterialTheme.shapes,
            scheme = MaterialTheme.colorScheme
        )

    }

}