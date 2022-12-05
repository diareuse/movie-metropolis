package movie.metropolis.app.screen.profile

import movie.metropolis.app.model.UserView

class ProfileFacadeCaching(
    private val origin: ProfileFacade
) : ProfileFacade by origin {

    private var user: UserView? = null

    override suspend fun getUser(): Result<UserView> {
        return user?.let(Result.Companion::success) ?: origin.getUser().onSuccess {
            user = it
        }
    }

    override suspend fun save(view: UserView): Result<UserView> {
        return origin.save(view).onSuccess {
            user = it
        }
    }

}