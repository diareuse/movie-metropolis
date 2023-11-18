package movie.style.animation

import android.view.animation.AnticipateOvershootInterpolator
import androidx.compose.animation.core.*

val AnticipateOvershootEasing = object : Easing {
    val easing = AnticipateOvershootInterpolator()
    override fun transform(fraction: Float): Float {
        return easing.getInterpolation(fraction)
    }
}