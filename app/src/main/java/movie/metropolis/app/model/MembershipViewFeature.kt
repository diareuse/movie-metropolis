package movie.metropolis.app.model

import movie.metropolis.app.feature.global.User
import java.text.DateFormat
import java.text.NumberFormat
import java.util.Date
import kotlin.time.Duration.Companion.milliseconds

data class MembershipViewFeature(
    private val user: User
) : MembershipView {

    private val date = DateFormat.getDateInstance(DateFormat.MEDIUM)
    private val number = NumberFormat.getNumberInstance()

    private val membership get() = checkNotNull(user.membership)

    override val isExpired: Boolean
        get() = membership.isExpired
    override val cardNumber: String
        get() = membership.cardNumber
    override val memberFrom: String
        get() = date.format(membership.memberFrom)
    override val memberUntil: String
        get() = date.format(membership.memberUntil)
    override val daysRemaining: String
        get() = (membership.memberUntil.time - Date().time).milliseconds.inWholeDays.toString() + "d"
    override val points: String
        get() = number.format(user.points)

}