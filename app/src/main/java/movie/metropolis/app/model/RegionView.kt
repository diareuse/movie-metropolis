package movie.metropolis.app.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.*

@Immutable
interface RegionView {

    val name: String

    @get:DrawableRes
    val icon: Int

}