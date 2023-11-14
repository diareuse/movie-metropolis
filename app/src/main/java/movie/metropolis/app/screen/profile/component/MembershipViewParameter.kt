package movie.metropolis.app.screen.profile.component

import androidx.compose.ui.tooling.preview.datasource.*
import movie.metropolis.app.model.MembershipView

class MembershipViewParameter : CollectionPreviewParameterProvider<MembershipView>(
    listOf(
        Data(),
        Data(isExpired = true)
    )
) {
    data class Data(
        override val isExpired: Boolean = false,
        override val cardNumber: String = "123456789",
        override val memberFrom: String = "Jan 1 2005",
        override val memberUntil: String = "Jan 1 2024",
        override val daysRemaining: String = "30 days",
        override val points: String = "100"
    ) : MembershipView
}