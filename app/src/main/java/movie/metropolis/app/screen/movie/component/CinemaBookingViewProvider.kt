package movie.metropolis.app.screen.movie.component

import androidx.compose.ui.tooling.preview.datasource.*
import movie.metropolis.app.model.AvailabilityView
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.model.CinemaView
import kotlin.random.Random.Default.nextBytes
import kotlin.random.Random.Default.nextInt

class CinemaBookingViewProvider :
    CollectionPreviewParameterProvider<CinemaBookingView>(
        listOf(
            CinemaBookingViewPreview(),
            CinemaBookingViewPreview()
        )
    ) {

    private data class CinemaBookingViewPreview(
        override val cinema: CinemaView = CinemaViewPreview(),
        override val availability: Map<AvailabilityView.Type, List<AvailabilityView>> = mapOf(
            LanguageAndTypePreview() to List(nextInt(1, 5)) { AvailabilityPreview() }
        )
    ) : CinemaBookingView

    private data class LanguageAndTypePreview(
        override val language: String = listOf(
            "English (Czech)",
            "Czech",
            "Hungarian (Czech)"
        ).random(),
        override val types: List<String> = listOf(
            listOf("2D"),
            listOf("3D"),
            listOf("3D", "4DX"),
            listOf("2D", "VIP")
        ).random()
    ) : AvailabilityView.Type

    private data class CinemaViewPreview(
        override val id: String = String(nextBytes(10)),
        override val name: String = "Some Cinema",
        override val address: String = "Foo bar 12/3",
        override val city: String = "City",
        override val distance: String? = null,
        override val image: String? = null,
        override val uri: String = ""
    ) : CinemaView

    private data class AvailabilityPreview(
        override val id: String = String(nextBytes(10)),
        override val url: String = "https://foo.bar",
        override val startsAt: String = "12:10",
        override val isEnabled: Boolean = true
    ) : AvailabilityView

}