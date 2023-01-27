package movie.metropolis.app.model

import androidx.compose.runtime.Immutable

@Immutable
interface MembershipView {
    val isExpired: Boolean
    val cardNumber: String
    val memberFrom: String
    val memberUntil: String
    val daysRemaining: String
    val points: String
}