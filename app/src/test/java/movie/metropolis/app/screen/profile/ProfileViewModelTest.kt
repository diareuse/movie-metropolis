package movie.metropolis.app.screen.profile

import movie.metropolis.app.screen.ViewModelTest

class ProfileViewModelTest : ViewModelTest() {

    private lateinit var viewModel: ProfileViewModel

    override fun prepare() {
        viewModel = ProfileViewModel()
    }

}