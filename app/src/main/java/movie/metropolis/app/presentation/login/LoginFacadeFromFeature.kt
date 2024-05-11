package movie.metropolis.app.presentation.login

import movie.cinema.city.CinemaCity
import movie.cinema.city.RegionProvider
import movie.cinema.city.UserDetails
import movie.cinema.city.requireRegion
import movie.core.auth.UserAccount

class LoginFacadeFromFeature(
    private val user: UserAccount,
    private val cinemaCity: CinemaCity,
    private val region: RegionProvider
) : LoginFacade {

    override val currentUserEmail get() = user.email
    override val domain: String
        get() = region.requireRegion().domain

    override suspend fun getPosters(): List<String> {
        return emptyList()
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<Unit> {
        user.email = email
        user.password = password
        return cinemaCity.runCatching { customers.getCustomer() }
            .map {}
            .onFailure {
                user.email = null
                user.password = null
            }
    }

    override suspend fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phone: String
    ): Result<Unit> {
        val details = UserDetails(
            email = email,
            firstName = firstName,
            lastName = lastName,
            password = password,
            phone = phone,
            marketing = false,
            premium = false
        )
        return cinemaCity.runCatching { create(details) }
            .map {}
            .onSuccess {
                user.email = email
                user.password = password
            }
    }

}