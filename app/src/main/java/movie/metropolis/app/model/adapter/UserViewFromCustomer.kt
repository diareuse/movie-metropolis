package movie.metropolis.app.model.adapter

import movie.cinema.city.Customer
import movie.metropolis.app.model.UserView

fun UserViewFromCustomer(
    customer: Customer,
) = UserView(customer.email).apply {
    firstName = customer.name.first
    lastName = customer.name.last
    phone = customer.phone
    favorite = customer.cinema?.let(::CinemaSimpleViewFromCinema)
    consent.marketing = customer.consent.marketing
    consent.premium = customer.consent.premium
}