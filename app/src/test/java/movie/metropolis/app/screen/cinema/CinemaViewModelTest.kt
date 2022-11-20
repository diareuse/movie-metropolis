package movie.metropolis.app.screen.cinema

import androidx.lifecycle.SavedStateHandle
import movie.metropolis.app.screen.ViewModelTest

class CinemaViewModelTest : ViewModelTest() {

    private lateinit var viewModel: CinemaViewModel

    override fun prepare() {
        viewModel = CinemaViewModel(SavedStateHandle(mapOf()))
    }

}