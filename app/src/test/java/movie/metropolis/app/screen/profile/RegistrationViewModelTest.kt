package movie.metropolis.app.screen.profile

import movie.metropolis.app.screen.ViewModelTest

class RegistrationViewModelTest : ViewModelTest() {

    private lateinit var viewModel: RegistrationViewModel

    override fun prepare() {
        viewModel = RegistrationViewModel()
    }

}