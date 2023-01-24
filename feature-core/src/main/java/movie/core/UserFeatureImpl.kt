package movie.core

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import movie.core.EventCinemaFeature.Companion.get
import movie.core.EventDetailFeature.Companion.get
import movie.core.adapter.BookingActiveFromResponse
import movie.core.adapter.BookingExpiredFromResponse
import movie.core.adapter.MovieFromId
import movie.core.adapter.UserFromRemote
import movie.core.auth.UserAccount
import movie.core.model.Booking
import movie.core.model.FieldUpdate
import movie.core.model.FieldUpdate.Cinema
import movie.core.model.FieldUpdate.Consent
import movie.core.model.FieldUpdate.Email
import movie.core.model.FieldUpdate.Name
import movie.core.model.FieldUpdate.Password
import movie.core.model.FieldUpdate.Phone
import movie.core.model.SignInMethod
import movie.core.model.User
import movie.core.nwk.UserService
import movie.core.nwk.model.CustomerDataRequest
import movie.core.nwk.model.CustomerResponse
import movie.core.nwk.model.PasswordRequest
import movie.core.nwk.model.RegistrationRequest
import movie.core.nwk.model.TokenRequest

internal class UserFeatureImpl(
    private val service: UserService,
    private val cinema: EventCinemaFeature,
    private val movie: EventDetailFeature,
    private val account: UserAccount
) : UserFeature {

    override val email get() = account.email

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
        return kotlin.runCatching {
            val cinemas = cinema.get(null).getOrDefault(emptyList()).toList()
            bookings.mapNotNull { booking ->
                val detail = movie.get(MovieFromId(booking.movieId)).getOrNull()
                when (booking.isExpired(detail?.duration!!)) {
                    true ->
                        if (detail == null) null
                        else BookingExpiredFromResponse(
                            response = booking,
                            movie = detail,
                            cinemas = cinemas
                        )

                    else -> {
                        val bookingDetail = service.getBooking(booking.id).getOrThrow()
                        if (detail == null) null
                        else BookingActiveFromResponse(
                            response = booking,
                            detail = bookingDetail,
                            movie = detail,
                            cinemas = cinemas
                        )
                    }
                }
            }
        }
    }

    override suspend fun getToken(): Result<String> {
        return service.getCurrentToken()
    }

    // ---

    private suspend fun getUser(
        user: suspend () -> Result<CustomerResponse.Customer>,
        password: suspend () -> Result<Unit> = { Result.success(Unit) },
    ): Result<User> = coroutineScope {
        val customer = async { user() }
        val points = async { service.getPoints() }
        val cinemas = cinema.get(null).map { it.toList() }
        password().mapCatching {
            UserFromRemote(
                customer.await().getOrThrow(),
                points.await().getOrThrow(),
                cinemas.getOrThrow()
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