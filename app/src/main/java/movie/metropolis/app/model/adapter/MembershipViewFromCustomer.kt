package movie.metropolis.app.model.adapter

import movie.cinema.city.Customer
import movie.metropolis.app.model.MembershipView
import java.util.Date
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit

fun MembershipViewFromCustomer(
    membership: Customer.Membership
) = MembershipView(membership.number).apply {
    isExpired = membership.expiration.before(Date())
    memberFrom = membership.inception.toString()
    memberUntil = membership.expiration.toString()
    daysRemaining = (membership.expiration.time - Date().time).days.toString(DurationUnit.DAYS)
    points = membership.points.total.toString()
}