package movie.metropolis.app.feature.user

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import movie.metropolis.app.feature.global.CinemaFromResponse
import movie.metropolis.app.feature.global.CinemaService
import movie.metropolis.app.feature.user.FieldUpdate.*
import movie.metropolis.app.feature.user.model.BookingDetailResponse
import movie.metropolis.app.feature.user.model.BookingResponse
import movie.metropolis.app.feature.user.model.CustomerDataRequest
import movie.metropolis.app.feature.user.model.CustomerPointsResponse
import movie.metropolis.app.feature.user.model.CustomerResponse
import movie.metropolis.app.feature.user.model.PasswordRequest
import movie.metropolis.app.feature.user.model.RegistrationRequest
import movie.metropolis.app.feature.user.model.TokenRequest
import java.util.Date
import kotlin.time.Duration.Companion.minutes

internal class UserFeatureImpl(
    private val service: UserService,
    private val account: UserAccount,
    private val cinema: CinemaService
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
        val cinemas = cinema.getCinemas().map { it.results }
            .getOrDefault(emptyList())
            .map(::CinemaFromResponse)
        return service.getBookings().map {
            it.map { booking ->
                when (booking.isExpired) {
                    true -> BookingExpiredFromResponse(booking, cinemas)
                    else -> when (val detail = service.getBooking(booking.id).getOrNull()) {
                        null -> BookingExpiredFromResponse(booking, cinemas)
                        else -> BookingActiveFromResponse(booking, detail, cinemas)
                    }
                }
            }
        }
    }

    override suspend fun getToken(): Result<String> {
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
        val userResponse = async { user() }
        val points = async { service.getPoints() }
        password().mapCatching { asUser(userResponse, points) }
    }

    @Throws(Throwable::class)
    private suspend fun asUser(
        user: Deferred<Result<CustomerResponse.Customer>>,
        points: Deferred<Result<CustomerPointsResponse>>
    ): User {
        val customer = user.await().getOrThrow()
        val consent = User.Consent(
            marketing = customer.consent.marketing,
            premium = customer.consent.marketingPremium ?: false
        )
        val membership = customer.membership.club?.let { club ->
            User.Membership(
                cardNumber = club.cardNumber,
                memberFrom = club.joinedAt,
                memberUntil = club.expiredAt
            )
        }
        return User(
            firstName = customer.firstName,
            lastName = customer.lastName,
            email = customer.email,
            phone = customer.phone,
            birthAt = customer.birthAt,
            favoriteCinemaId = TODO(),
            consent = consent,
            membership = membership,
            points = points.await().getOrThrow().total
        )
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

internal data class BookingExpiredFromResponse(
    private val response: BookingResponse,
    override val cinema: movie.metropolis.app.feature.global.Cinema
) : Booking.Expired {

    constructor(
        response: BookingResponse,
        cinemas: List<movie.metropolis.app.feature.global.Cinema>
    ) : this(
        response,
        cinemas.first { it.id == response.cinemaId }
    )

    override val id: String
        get() = response.id
    override val name: String
        get() = response.name
    override val startsAt: Date
        get() = response.startsAt
    override val paidAt: Date
        get() = response.paidAt
    override val distributorCode: String
        get() = response.eventMasterCode
    override val eventId: String
        get() = response.eventId

}

internal data class BookingActiveFromResponse(
    private val response: BookingResponse,
    private val detail: BookingDetailResponse,
    override val cinema: movie.metropolis.app.feature.global.Cinema
) : Booking.Active {

    constructor(
        response: BookingResponse,
        detail: BookingDetailResponse,
        cinemas: List<movie.metropolis.app.feature.global.Cinema>
    ) : this(
        response,
        detail,
        cinemas.first { it.id == response.cinemaId }
    )

    override val id: String
        get() = response.id
    override val name: String
        get() = response.name
    override val startsAt: Date
        get() = response.startsAt
    override val paidAt: Date
        get() = response.paidAt
    override val distributorCode: String
        get() = response.eventMasterCode
    override val eventId: String
        get() = response.eventId
    override val hall: String
        get() = detail.hall
    override val seats: List<Booking.Active.Seat>
        get() = detail.tickets.map(::SeatFromResponse)

    private data class SeatFromResponse(
        private val ticket: BookingDetailResponse.Ticket
    ) : Booking.Active.Seat {

        override val row: String
            get() = ticket.row
        override val seat: String
            get() = ticket.seat

    }
}