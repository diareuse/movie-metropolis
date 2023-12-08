@file:OptIn(ExperimentalMaterial3Api::class)

package movie.metropolis.app.screen.space

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import movie.metropolis.app.model.DeleteProgress
import movie.style.Container
import movie.style.DialogBox
import movie.style.theme.Theme

abstract class AbstractSpaceActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        super.onCreate(savedInstanceState)
        setContent {
            Theme {
                Surface {
                    val viewModel = hiltViewModel<SpaceViewModel>()
                    val state by viewModel.state.collectAsState()
                    var progress by remember { mutableStateOf(null as DeleteProgress?) }
                    DialogBox(
                        visible = progress != null,
                        dialog = {
                            Container(onDismissRequest = {}) {
                                DeleteDialog(progress = progress)
                            }
                        }
                    ) {
                        val scope = rememberCoroutineScope()
                        SpaceScreen(
                            state = state,
                            onClickBack = ::finish,
                            onDeleteMoviesClick = {
                                scope.launch {
                                    viewModel.deleteCore().collect {
                                        progress = it
                                    }
                                    progress = null
                                }
                            },
                            onDeletePosterClick = {
                                scope.launch {
                                    viewModel.deletePosters().collect {
                                        progress = it
                                    }
                                    progress = null
                                }
                            },
                            onDeleteRatingsClick = {
                                scope.launch {
                                    viewModel.deleteRating().collect {
                                        progress = it
                                    }
                                    progress = null
                                }
                            }
                        )
                    }
                }
            }
        }
    }

}