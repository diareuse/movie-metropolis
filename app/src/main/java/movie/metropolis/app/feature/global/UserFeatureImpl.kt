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
import movie.metropolis.app.feature.global.model.BookingDetailResponse
import movie.metropolis.app.feature.global.model.BookingResponse
import movie.metropolis.app.feature.global.model.ConsentRemote
import movie.metropolis.app.feature.global.model.CustomerDataRequest
import movie.metropolis.app.feature.global.model.CustomerPointsResponse
import movie.metropolis.app.feature.global.model.CustomerResponse
import movie.metropolis.app.feature.global.model.PasswordRequest
import movie.metropolis.app.feature.global.model.RegistrationRequest
import movie.metropolis.app.feature.global.model.TokenRequest
import movie.metropolis.app.model.MovieFromId
import java.util.Date
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

internal data class UserFromRemote(
    private val customer: CustomerResponse.Customer,
    private val customerPoints: CustomerPointsResponse,
    override val favorite: movie.metropolis.app.feature.global.Cinema?
) : User {

    constructor(
        customer: CustomerResponse.Customer,
        points: CustomerPointsResponse,
        cinemas: List<movie.metropolis.app.feature.global.Cinema>
    ) : this(
        customer = customer,
        customerPoints = points,
        favorite = cinemas.firstOrNull { it.id == customer.favoriteCinema }
    )

    override val firstName: String
        get() = customer.firstName
    override val lastName: String
        get() = customer.lastName
    override val email: String
        get() = customer.email
    override val phone: String
        get() = customer.phone
    override val consent: User.Consent
        get() = ConsentFromRemote(customer.consent)
    override val membership: User.Membership?
        get() = customer.membership.club?.let(UserFromRemote::MembershipFromRemote)
    override val points: Double
        get() = customerPoints.total

    private data class ConsentFromRemote(
        private val consent: ConsentRemote
    ) : User.Consent {
        override val marketing: Boolean
            get() = consent.marketing
        override val premium: Boolean
            get() = consent.marketingPremium ?: false
    }

    private data class MembershipFromRemote(
        private val club: CustomerResponse.Club
    ) : User.Membership {
        override val cardNumber: String
            get() = club.cardNumber
        override val memberFrom: Date
            get() = club.joinedAt
        override val memberUntil: Date
            get() = club.expiredAt
    }

}

internal data class BookingExpiredFromResponse(
    private val response: BookingResponse,
    override val movie: MovieDetail,
    override val cinema: movie.metropolis.app.feature.global.Cinema
) : Booking.Expired {

    constructor(
        response: BookingResponse,
        movie: MovieDetail,
        cinemas: List<movie.metropolis.app.feature.global.Cinema>
    ) : this(
        response = response,
        movie = movie,
        cinema = cinemas.first { it.id == response.cinemaId }
    )

    override val id: String
        get() = response.id
    override val name: String
        get() = response.name
    override val startsAt: Date
        get() = response.startsAt
    override val paidAt: Date
        get() = response.paidAt
    override val eventId: String
        get() = response.eventId

}

internal data class BookingActiveFromResponse(
    private val response: BookingResponse,
    private val detail: BookingDetailResponse,
    override val movie: MovieDetail,
    override val cinema: movie.metropolis.app.feature.global.Cinema
) : Booking.Active {

    constructor(
        response: BookingResponse,
        detail: BookingDetailResponse,
        movie: MovieDetail,
        cinemas: List<movie.metropolis.app.feature.global.Cinema>
    ) : this(
        response = response,
        detail = detail,
        movie = movie,
        cinema = cinemas.first { it.id == response.cinemaId }
    )

    override val id: String
        get() = response.id
    override val name: String
        get() = response.name
    override val startsAt: Date
        get() = response.startsAt
    override val paidAt: Date
        get() = response.paidAt
    override val eventId: String
        get() = response.eventId
    override val hall: String
        get() = detail.hall
    override val seats: List<Booking.Active.Seat>
        get() = detail.tickets.map(BookingActiveFromResponse::SeatFromResponse)

    private data class SeatFromResponse(
        private val ticket: BookingDetailResponse.Ticket
    ) : Booking.Active.Seat {

        override val row: String
            get() = ticket.row
        override val seat: String
            get() = ticket.seat

    }
}