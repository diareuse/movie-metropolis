package movie.metropolis.app.presentation.home

import movie.core.auth.UserAccount

class HomeFacadeFromFeature(
    private val user: UserAccount
) : HomeFacade {

    override val email get() = user.email

}