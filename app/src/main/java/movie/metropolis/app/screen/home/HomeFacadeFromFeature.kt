package movie.metropolis.app.screen.home

import movie.core.UserFeature

class HomeFacadeFromFeature(
    private val user: UserFeature
) : HomeFacade {

    override val email get() = user.email

}