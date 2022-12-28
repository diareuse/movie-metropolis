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
            Color.RGBToHSV(color.red, color.green, color.blue, this)
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

}