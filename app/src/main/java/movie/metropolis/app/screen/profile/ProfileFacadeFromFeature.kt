package movie.metropolis.app.screen.profile

import movie.metropolis.app.feature.global.EventFeature
import movie.metropolis.app.feature.global.UserFeature
import movie.metropolis.app.feature.global.model.FieldUpdate
import movie.metropolis.app.model.CinemaSimpleView
import movie.metropolis.app.model.MembershipView
import movie.metropolis.app.model.UserView
import movie.metropolis.app.model.adapter.CinemaSimpleViewFromFeature
import movie.metropolis.app.model.adapter.MembershipViewFeature
import movie.metropolis.app.model.adapter.UserViewFromFeature

class ProfileFacadeFromFeature(
    private val user: UserFeature,
    private val event: EventFeature
) : ProfileFacade {

    override suspend fun getCinemas(): Result<List<CinemaSimpleView>> {
        return event.getCinemas(null).map { it.map(::CinemaSimpleViewFromFeature) }
    }

    override suspend fun getMembership(): Result<MembershipView?> {
        return user.getUser()
            .map { it.let(::MembershipViewFeature).takeIf { _ -> it.membership != null } }
    }

    override suspend fun getUser(): Result<UserView> {
        return user.getUser().map(::UserViewFromFeature)
    }

    override suspend fun isLoggedIn(): Boolean {
        return user.getToken().isSuccess
    }

    override suspend fun save(view: UserView): Result<UserView> {
        val currentUser = getUser().getOrThrow()
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
            return Result.failure(IllegalArgumentException())
        return user.update(fields).map(::UserViewFromFeature)
    }

    override suspend fun save(passwordOld: String, passwordNew: String): Result<Unit> {
        check(passwordOld.isNotBlank())
        check(passwordNew.isNotBlank())
        check(passwordNew != passwordOld)
        val fields = listOf(FieldUpdate.Password(passwordOld, passwordNew))
        return user.update(fields).map {}
    }

}