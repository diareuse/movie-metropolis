package movie.core

import movie.core.adapter.UserFromRemote
import movie.core.model.FieldUpdate
import movie.core.model.User
import movie.core.nwk.UserService
import movie.core.nwk.model.CustomerDataRequest

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

    override suspend fun get(): User {
        val user = service.getUser().getOrThrow()
        val cinema = cinema.get(null)
        val points = service.getPoints().getOrThrow()
        return UserFromRemote(
            customer = user,
            customerPoints = points,
            favorite = cinema.firstOrNull { it.id == user.favoriteCinema }
        )
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