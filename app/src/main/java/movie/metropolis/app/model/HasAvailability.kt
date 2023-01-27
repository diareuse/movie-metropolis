package movie.metropolis.app.model

import androidx.compose.runtime.Stable

@Stable
interface HasAvailability {

    val availability: Map<AvailabilityView.Type, List<AvailabilityView>>

}