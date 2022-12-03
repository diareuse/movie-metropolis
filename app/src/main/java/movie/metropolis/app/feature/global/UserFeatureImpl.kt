package movie.metropolis.app.feature.global

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import movie.metropolis.app.feature.global.FieldUpdate.Cinema
import movie.metropolis.app.feature.global.FieldUpdate.Consent
import movie.metropolis.app.feature.global.FieldUpdate.Email
import movie.metropolis.app.feature.global.FieldUpdate.Name
import movie.metropolis.app.feature.global.FieldUpdate.Password
import movie.metropolis.app.feature.global.FieldUpdate.Phone
import movie.metropolis.app.feature.global.adapter.BookingActiveFromResponse
import movie.metropolis.app.feature.global.adapter.BookingExpiredFromResponse
import movie.metropolis.app.feature.global.adapter.UserFromRemote
import movie.metropolis.app.feature.global.model.CustomerDataRequest
import movie.metropolis.app.feature.global.model.CustomerResponse
import movie.metropolis.app.feature.global.model.PasswordRequest
import movie.metropolis.app.feature.global.model.RegistrationRequest
import movie.metropolis.app.feature.global.model.TokenRequest
import movie.metropolis.app.model.MovieFromId
import kotlin.time.Duration.Companion.minutes

internal class UserFeatureImpl(
    private val service: UserService,
    private val account: UserAccount,
    private val event: EventFeature
) : UserFeature {

    override suspend fun signIn(method: SignInMethod) = when (method) {
        is SignInMethod.Login -> service.getToken(
            TokenRequest.Login(
                username = method.email,
                password = method.password
            )
        ).map {}

        is SignInMethod.Registration -> service.register(
            RegistrationRequest(
                email = method.email,
                firstName = method.firstName,
                lastName = method.lastName,
                password = method.password,
                phone = method.phone
            )
        ).map {}
    }

    override suspend fun update(data: Iterable<FieldUpdate>) = getUser(
        user = {
            service.getUser()
                .map { CustomerDataRequest(it) }
                .map { data.fold(it, ::updateRequest) }
                .mapCatching { service.updateUser(it).getOrThrow().customer }
        },
        password = {
            val passwordRequest = data.asSequence()
                .filterIsInstance<Password>()
                .map { PasswordRequest(it.old, it.new) }
                .firstOrNull()
            if (passwordRequest != null) service.updatePassword(passwordRequest)
            else Result.success(Unit)
        }
    )

    override suspend fun getUser(): Result<User> {
        return getUser(user = { service.getUser() })
    }

    override suspend fun getBookings(): Result<Iterable<Booking>> {
        val bookingsResult = service.getBookings()
        if (bookingsResult.isFailure)
            return bookingsResult as Result<Iterable<Booking>>
        val bookings = bookingsResult.getOrThrow()
        return coroutineScope {
            val cinemas =
                async { event.getCinemas(null).map { it.toList() }.getOrDefault(emptyList()) }

            bookings.map { booking ->
                val movie = async { event.getDetail(MovieFromId(booking.movieId)).getOrNull() }
                when (booking.isExpired) {
                    true -> async {
                        val detail = movie.await()
                        if (detail == null) null
                        else BookingExpiredFromResponse(
                            response = booking,
                            movie = detail,
                            cinemas = cinemas.await()
                        )
                    }

                    else -> {
                        val detail = async { service.getBooking(booking.id).getOrThrow() }
                        async {
                            val movieDetail = movie.await()
                            if (movieDetail == null) null
                            else BookingActiveFromResponse(
                                response = booking,
                                detail = detail.await(),
                                movie = movieDetail,
                                cinemas = cinemas.await()
                            )
                        }
                    }
                }
            }.runCatching { awaitAll().filterNotNull() }
        }
    }

    override suspend fun getToken(): Result<String> {
        if (!account.isLoggedIn) return Result.failure(IllegalStateException())
        if (account.isExpired || account.expiresWithin(1.minutes)) {
            service.getPoints() // this just refreshes the token
        }
        return kotlin.runCatching { checkNotNull(account.token) }
    }

    // ---

    private suspend fun getUser(
        user: suspend () -> Result<CustomerResponse.Customer>,
        password: suspend () -> Result<Unit> = { Result.success(Unit) },
    ): Result<User> = coroutineScope {
        val customer = async { user() }
        val points = async { service.getPoints() }
        val cinemas = async { event.getCinemas(null).map { it.toList() } }
        password().mapCatching {
            UserFromRemote(
                customer.await().getOrThrow(),
                points.await().getOrThrow(),
                cinemas.await().getOrThrow()
            )
        }
    }

    // ---

    private fun updateRequest(model: CustomerDataRequest, field: FieldUpdate) = when (field) {
        is Cinema -> model.copy(favoriteCinema = field.id)
        is Consent.Marketing -> model.copy(consent = model.consent.copy(marketing = field.isEnabled))
        is Consent.Premium -> model.copy(consent = model.consent.copy(marketingPremium = field.isEnabled))
        is Email -> model.copy(email = field.value)
        is Name.First -> model.copy(firstName = field.value)
        is Name.Last -> model.copy(lastName = field.value)
        is Phone -> model.copy(phone = field.value)
        else -> model
    }
}