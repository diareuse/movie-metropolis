package movie.metropolis.app.screen.booking

import movie.metropolis.app.screen.ViewModelTest

class BookingViewModelTest : ViewModelTest() {

    private lateinit var viewModel: BookingViewModel

    override fun prepare() {
        viewModel = BookingViewModel()
    }

}