package movie.metropolis.app.model.adapter

import movie.cinema.city.Customer
import movie.metropolis.app.model.MembershipView
import java.text.DateFormat
import java.util.Date
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit

fun MembershipViewFromCustomer(
    membership: Customer.Membership,
    format: DateFormat
) = MembershipView(membership.number.windowed(4, 4).joinToString(" ")).apply {
    isExpired = membership.expiration.before(Date())
    memberFrom = format.format(membership.inception)
    memberUntil = format.format(membership.expiration)
    daysRemaining = (membership.expiration.time - Date().time).days.toString(DurationUnit.DAYS)
    points = membership.points.total.toString()
}