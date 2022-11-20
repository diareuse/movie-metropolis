package movie.metropolis.app.screen.detail

import androidx.lifecycle.SavedStateHandle
import movie.metropolis.app.screen.ViewModelTest

class MovieViewModelTest : ViewModelTest() {

    private lateinit var viewModel: MovieViewModel

    override fun prepare() {
        viewModel = MovieViewModel(SavedStateHandle(mapOf()))
    }

}