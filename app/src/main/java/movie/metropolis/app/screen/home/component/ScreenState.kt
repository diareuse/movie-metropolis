package movie.metropolis.app.screen.home.component

import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel

@OptIn(ExperimentalMaterial3Api::class)
data class ScreenState<T>(
    val behavior: TopAppBarScrollBehavior,
    val list: LazyListState,
    val viewModel: T
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
inline fun <reified V : ViewModel> rememberScreenState(): ScreenState<V> {
    return ScreenState(
        behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
        list = rememberLazyListState(),
        viewModel = hiltViewModel()
    )
}