package movie.metropolis.app.screen

import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

val NavHostController.currentDestinationFlow
    get() = callbackFlow {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            trySend(destination)
        }
        addOnDestinationChangedListener(listener)
        awaitClose {
            removeOnDestinationChangedListener(listener)
        }
    }

@Composable
fun NavHostController.currentDestinationAsState(): State<NavDestination?> {
    return currentDestinationFlow.collectAsState(currentDestination)
}