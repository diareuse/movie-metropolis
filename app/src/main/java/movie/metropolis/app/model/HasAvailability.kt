package movie.metropolis.app.model

interface HasAvailability {

    val availability: Map<AvailabilityView.Type, List<AvailabilityView>>

}