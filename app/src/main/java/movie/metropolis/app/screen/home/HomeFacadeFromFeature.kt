package movie.metropolis.app.screen.home

import movie.core.UserCredentialFeature

class HomeFacadeFromFeature(
    private val user: UserCredentialFeature
) : HomeFacade {

    override val email get() = user.email

}