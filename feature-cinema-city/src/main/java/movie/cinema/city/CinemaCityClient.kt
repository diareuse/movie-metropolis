package movie.cinema.city

import io.ktor.client.plugins.auth.providers.BearerTokens
import movie.cinema.city.model.BookingDetailResponse
import movie.cinema.city.model.BookingResponse
import movie.cinema.city.model.CinemaResponse
import movie.cinema.city.model.CustomerDataRequest
import movie.cinema.city.model.CustomerPointsResponse
import movie.cinema.city.model.CustomerResponse
import movie.cinema.city.model.ExtendedMovieResponse
import movie.cinema.city.model.MovieDetailResponse
import movie.cinema.city.model.MovieEventResponse
import movie.cinema.city.model.PasswordRequest
import movie.cinema.city.model.PromoCardResponse
import movie.cinema.city.model.RegistrationRequest
import movie.cinema.city.model.ShowingType
import java.util.Date

internal interface CinemaCityClient {
    suspend fun register(request: RegistrationRequest): CustomerResponse
    suspend fun updatePassword(request: PasswordRequest)
    suspend fun updateUser(request: CustomerDataRequest): CustomerResponse
    suspend fun getPoints(): CustomerPointsResponse
    suspend fun getUser(): CustomerResponse.Customer
    suspend fun getBookings(): List<BookingResponse>
    suspend fun getBooking(id: String): BookingDetailResponse
    suspend fun getCinemas(): List<CinemaResponse>
    suspend fun getPromoCards(): List<PromoCardResponse>
    suspend fun getEventsInCinema(cinema: String, date: Date): MovieEventResponse
    suspend fun getDetail(id: String): MovieDetailResponse
    suspend fun getMoviesByType(type: ShowingType): List<ExtendedMovieResponse>
    suspend fun getToken(): BearerTokens
}