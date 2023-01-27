package movie.metropolis.app.screen.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import movie.metropolis.app.presentation.home.HomeFacade
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val facade: HomeFacade
) : ViewModel() {

    val email get() = facade.email

}