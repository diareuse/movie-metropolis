package movie.metropolis.app.screen.listing

import movie.metropolis.app.screen.ViewModelTest

class ListingViewModelTest : ViewModelTest() {

    private lateinit var viewModel: ListingViewModel

    override fun prepare() {
        viewModel = ListingViewModel()
    }

}