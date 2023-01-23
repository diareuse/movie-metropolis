package movie.core

import movie.core.EventCinemaFeature.Companion.get
import movie.core.adapter.UserFromRemote
import movie.core.model.FieldUpdate
import movie.core.model.User
import movie.core.nwk.UserService
import movie.core.nwk.model.CustomerDataRequest
import movie.core.nwk.model.CustomerPointsResponse
import java.util.Date

class UserDataFeatureNetwork(
    private val service: UserService,
    private val cinema: EventCinemaFeature
) : UserDataFeature {

    override suspend fun update(data: Iterable<FieldUpdate>) {
        val customer = service.getUser().getOrThrow()
        val request = CustomerDataRequest(customer).run {
            data.fold(this, ::updateRequest)
        }
        service.updateUser(request)
    }

    override suspend fun get(callback: ResultCallback<User>) {
        val user = service.getUser().getOrThrow()
        val cinema = cinema.get(null).getOrThrow()
        var output = UserFromRemote(
            customer = user,
            customerPoints = CustomerPointsResponse(0.0, 0.0, Date()),
            favorite = cinema.firstOrNull { it.id == user.favoriteCinema }
        )
        callback(Result.success(output))
        val points = service.getPoints().getOrThrow()
        output = output.copy(customerPoints = points)
        callback(Result.success(output))
    }

    // ---

    private fun updateRequest(model: CustomerDataRequest, field: FieldUpdate) = when (field) {
        is FieldUpdate.Cinema -> model.copy(favoriteCinema = field.id)
        is FieldUpdate.Consent.Marketing -> model.copy(consent = model.consent.copy(marketing = field.isEnabled))
        is FieldUpdate.Consent.Premium -> model.copy(consent = model.consent.copy(marketingPremium = field.isEnabled))
        is FieldUpdate.Email -> model.copy(email = field.value)
        is FieldUpdate.Name.First -> model.copy(firstName = field.value)
        is FieldUpdate.Name.Last -> model.copy(lastName = field.value)
        is FieldUpdate.Phone -> model.copy(phone = field.value)
        else -> model
    }

}