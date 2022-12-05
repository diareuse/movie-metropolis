package movie.metropolis.app.screen.profile

import movie.metropolis.app.model.CinemaSimpleView
import movie.metropolis.app.model.MembershipView
import movie.metropolis.app.model.UserView

class ProfileFacadeRecover(
    private val origin: ProfileFacade
) : ProfileFacade {

    override suspend fun getCinemas(): Result<List<CinemaSimpleView>> {
        return kotlin.runCatching { origin.getCinemas().getOrThrow() }
    }

    override suspend fun getMembership(): Result<MembershipView?> {
        return kotlin.runCatching { origin.getMembership().getOrThrow() }
    }

    override suspend fun getUser(): Result<UserView> {
        return kotlin.runCatching { origin.getUser().getOrThrow() }
    }

    override suspend fun isLoggedIn(): Boolean {
        return kotlin.runCatching { origin.isLoggedIn() }.getOrDefault(false)
    }

    override suspend fun save(view: UserView): Result<UserView> {
        return kotlin.runCatching { origin.save(view).getOrThrow() }
    }

    override suspend fun save(passwordOld: String, passwordNew: String): Result<Unit> {
        return kotlin.runCatching { origin.save(passwordOld, passwordNew).getOrThrow() }
    }

}