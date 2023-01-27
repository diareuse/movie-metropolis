package movie.metropolis.app.model

import androidx.compose.runtime.Immutable

@Immutable
interface HasAvailability {

    val availability: Map<AvailabilityView.Type, List<AvailabilityView>>

}