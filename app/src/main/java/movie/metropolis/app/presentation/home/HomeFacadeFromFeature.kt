package movie.metropolis.app.presentation.home

import movie.core.UserCredentialFeature

class HomeFacadeFromFeature(
    private val user: UserCredentialFeature
) : HomeFacade {

    override val email get() = user.email

}