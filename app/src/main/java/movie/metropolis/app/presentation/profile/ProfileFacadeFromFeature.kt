package movie.metropolis.app.presentation.profile

import movie.core.EventCinemaFeature
import movie.core.ResultCallback
import movie.core.UserCredentialFeature
import movie.core.UserDataFeature
import movie.core.model.FieldUpdate
import movie.metropolis.app.model.CinemaSimpleView
import movie.metropolis.app.model.MembershipView
import movie.metropolis.app.model.UserView
import movie.metropolis.app.model.adapter.CinemaSimpleViewFromFeature
import movie.metropolis.app.model.adapter.MembershipViewFeature
import movie.metropolis.app.model.adapter.UserViewFromFeature

class ProfileFacadeFromFeature(
    private val user: UserDataFeature,
    private val cinemas: EventCinemaFeature,
    private val credential: UserCredentialFeature
) : ProfileFacade {

    override suspend fun getCinemas(callback: ResultCallback<List<CinemaSimpleView>>) {
        val output = cinemas.get(null).map { result ->
            result.map(::CinemaSimpleViewFromFeature).toList()
        }
        callback(output)
    }

    override suspend fun getMembership(callback: ResultCallback<MembershipView?>) {
        user.get { result ->
            val output = result.map {
                it.let(::MembershipViewFeature).takeIf { _ -> it.membership != null }
            }
            callback(output)
        }
    }

    override suspend fun getUser(callback: ResultCallback<UserView>) {
        user.get { result ->
            val output = result.map(::UserViewFromFeature)
            callback(output)
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        return credential.getToken().isSuccess
    }

    override suspend fun save(view: UserView) {
        var value: UserView? = null
        getUser { value = it.getOrNull() }
        val currentUser = checkNotNull(value)
        val fields = buildList {
            if (currentUser.firstName != view.firstName)
                this += FieldUpdate.Name.First(view.firstName)
            if (currentUser.lastName != view.lastName)
                this += FieldUpdate.Name.Last(view.lastName)
            if (currentUser.email != view.email)
                this += FieldUpdate.Email(view.email)
            if (currentUser.phone != view.phone)
                this += FieldUpdate.Phone(view.phone)
            if (currentUser.consent.marketing != view.consent.marketing)
                this += FieldUpdate.Consent.Marketing(view.consent.marketing)
            if (currentUser.consent.premium != view.consent.premium)
                this += FieldUpdate.Consent.Premium(view.consent.premium)
            val favorite = view.favorite?.id
            if (favorite != null) if (currentUser.favorite?.id != favorite)
                this += FieldUpdate.Cinema(favorite)
        }
        if (fields.isEmpty())
            return
        user.update(fields)
    }

    override suspend fun save(passwordOld: String, passwordNew: String) {
        val field = FieldUpdate.Password(passwordOld, passwordNew)
        check(field.isValid)
        user.update(listOf(field))
    }

}