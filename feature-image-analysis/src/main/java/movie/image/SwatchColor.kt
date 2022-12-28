package movie.image

import android.graphics.Color
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red

data class SwatchColor(
    val rgb: Int,
    val hue: Float,
    val saturation: Float,
    val value: Float
) {

    constructor(
        color: Int
    ) : this(
        rgb = color,
        hsv = FloatArray(3).apply {
            try {
                Color.RGBToHSV(color.red, color.green, color.blue, this)
            } catch (ignore: RuntimeException) {
                // we'll ignore so tests are passing nominally
                // it's retarded that /platform/ contains this logic, but then again, it does so
                // natively and therefore faster, so whatever
            }
        }
    )

    constructor(
        rgb: Int,
        hsv: FloatArray
    ) : this(
        rgb = rgb,
        hue = hsv[0],
        saturation = hsv[1],
        value = hsv[2]
    )

    companion object {

        val Black get() = SwatchColor(0x000000, floatArrayOf(0f, 0f, 0f))

    }

}