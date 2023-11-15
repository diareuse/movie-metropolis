package movie.metropolis.app.screen.settings.component

import androidx.compose.ui.text.*
import androidx.compose.ui.text.input.*

object LengthVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(text + AnnotatedString(" km"), CeilOffsetMapping(text.length))
    }

    class CeilOffsetMapping(private val ceil: Int) : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return offset
        }

        override fun transformedToOriginal(offset: Int): Int {
            return offset.coerceAtMost(ceil)
        }
    }

}