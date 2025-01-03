package movie.metropolis.app.screen.setup.component

import androidx.compose.ui.tooling.preview.datasource.*
import movie.metropolis.app.model.MembershipView

class MembershipViewParameter : CollectionPreviewParameterProvider<MembershipView>(
    listOf(
        Data(),
        Data(isExpired = true)
    )
) {
    companion object {
        fun Data(isExpired: Boolean = false) = MembershipView("123456789").apply {
            memberFrom = "Jan 1 2005"
            memberUntil = "Jan 1 2024"
            daysRemaining = "30 days"
            points = "100"
            this.isExpired = isExpired
        }
    }
}