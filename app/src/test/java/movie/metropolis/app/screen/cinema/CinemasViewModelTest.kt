package movie.metropolis.app.screen.cinema

import movie.metropolis.app.screen.ViewModelTest

class CinemasViewModelTest : ViewModelTest() {

    private lateinit var viewModel: CinemasViewModel

    override fun prepare() {
        viewModel = CinemasViewModel()
    }

}