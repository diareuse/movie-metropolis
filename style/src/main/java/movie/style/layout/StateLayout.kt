package movie.style.layout

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*

@JvmInline
value class LayoutState<T : Any> private constructor(private val state: Any?) {
    val loading get() = state === Loading
    val error get() = state == null

    @Suppress("UNCHECKED_CAST")
    fun get() = state as T

    @Suppress("UNCHECKED_CAST")
    fun getOrNull() = if (error || loading) null else state as T

    private object Loading
    companion object {
        fun <T : Any> loading() = LayoutState<T>(Loading)
        fun <T : Any> result(value: T?) = LayoutState<T>(value)
    }
}

@Composable
inline fun <T : Any> StateLayout(
    state: LayoutState<T>,
    loaded: @Composable (T) -> Unit,
    error: @Composable () -> Unit,
    loading: @Composable () -> Unit,
) = when {
    state.loading -> loading()
    state.error -> error()
    else -> loaded(state.get())
}

@Preview
@Composable
private fun StateLayoutPreview(
    @PreviewParameter(LayoutStateProvider::class)
    value: LayoutStateProvider.Wrapper
) = PreviewLayout {
    StateLayout(
        state = value.state,
        loaded = { Box(modifier = Modifier.background(Color.Green)) },
        error = { Box(modifier = Modifier.background(Color.Red)) },
        loading = { Box(contentAlignment = Alignment.Center) { CircularProgressIndicator() } }
    )
}

class LayoutStateProvider : CollectionPreviewParameterProvider<LayoutStateProvider.Wrapper>(
    listOf(
        Wrapper(LayoutState.loading<Any>()),
        Wrapper(LayoutState.result(null)),
        Wrapper(LayoutState.result(""))
    )
) {
    class Wrapper(val state: LayoutState<*>)
}
