package movie.metropolis.app.model

import androidx.compose.runtime.*

data class MembershipView(
    val cardNumber: String
) {

    var isExpired by mutableStateOf(true)
    var memberFrom by mutableStateOf("")
    var memberUntil by mutableStateOf("")
    var daysRemaining by mutableStateOf("")
    var points by mutableStateOf("")

}