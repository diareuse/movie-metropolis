package movie.metropolis.app.screen.profile

import movie.metropolis.app.screen.ViewModelTest

class LoginViewModelTest : ViewModelTest() {

    private lateinit var viewModel: LoginViewModel

    override fun prepare() {
        viewModel = LoginViewModel()
    }

}