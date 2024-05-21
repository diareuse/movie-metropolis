package movie.metropolis.app.model.adapter

import movie.cinema.city.Customer
import movie.metropolis.app.model.MembershipView
import java.util.Date
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit

data class MembershipViewFromCustomer(
    private val membership: Customer.Membership
) : MembershipView {
    override val isExpired: Boolean
        get() = membership.expiration.before(Date())
    override val cardNumber: String
        get() = membership.number
    override val memberFrom: String
        get() = membership.inception.toString()
    override val memberUntil: String
        get() = membership.expiration.toString()
    override val daysRemaining: String
        get() = (membership.expiration.time - Date().time).days.toString(DurationUnit.DAYS)
    override val points: String
        get() = membership.points.total.toString()
}