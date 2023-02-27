package movie.metropolis.app.model

import androidx.compose.runtime.*

@Immutable
interface HasAvailability {

    val availability: Map<AvailabilityView.Type, List<AvailabilityView>>

}