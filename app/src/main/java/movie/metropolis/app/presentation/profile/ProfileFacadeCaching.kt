package movie.metropolis.app.presentation.profile

import movie.core.ResultCallback
import movie.metropolis.app.model.UserView

class ProfileFacadeCaching(
    private val origin: ProfileFacade
) : ProfileFacade by origin {

    private var user: UserView? = null

    override suspend fun getUser(callback: ResultCallback<UserView>) {
        val value = user
        if (value != null)
            return callback(Result.success(value))
        origin.getUser { result ->
            callback(result.onSuccess { user = it })
        }
    }

}