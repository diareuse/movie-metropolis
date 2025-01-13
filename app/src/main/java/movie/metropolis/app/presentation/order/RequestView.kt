package movie.metropolis.app.presentation.order

import androidx.compose.runtime.*

@Stable
class RequestView {
    var url by mutableStateOf("")
    val headers = mutableStateMapOf<String, String>()
}