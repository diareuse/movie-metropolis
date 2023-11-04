package movie.metropolis.app.screen

import androidx.compose.runtime.*
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import movie.metropolis.app.screen2.setup.SetupState
import movie.metropolis.app.util.decodeBase64
import movie.metropolis.app.util.encodeBase64

@Immutable
sealed class Route(
    val route: String
) {

    companion object {

        private const val InternalUri = "app://movie.metropolis"
        fun by(name: String) = when (name) {
            Cinema.route -> Cinema
            Cinemas.route -> Cinemas
            Home.route -> Home
            Login.route -> Login
            Movie.route -> Movie
            Movies.route -> Movies
            Order.route -> Order
            OrderComplete.route -> OrderComplete
            Settings.route -> Settings
            Setup.route -> Setup
            Tickets.route -> Tickets
            User.route -> User
            else -> throw IllegalArgumentException("Unknown route name $name")
        }

    }

    open val deepLinks = listOf(
        navDeepLink { uriPattern = "$InternalUri/$route" }
    )

    data object Setup : Route("setup?startWith={startWith}") {
        operator fun invoke(startWith: SetupState = SetupState.Initial) =
            route.replace("{startWith}", startWith.name)
    }

    data object Home : Route("home?screen={screen}") {
        val arguments = listOf(
            navArgument("screen") {
                type = NavType.StringType
                defaultValue = Movies.route
            }
        )

        class Arguments(private val entry: NavBackStackEntry) {
            val screen get() = entry.arguments?.getString("screen").let(::requireNotNull)
        }

        operator fun invoke(screen: String = Movies.route) =
            route.replace("{screen}", screen)

        fun deepLink(screen: String = Movies.route) =
            "$InternalUri/${invoke(screen)}".toUri()

    }

    data object User : Route("users/me") {
        operator fun invoke() = route
    }

    @Deprecated("")
    data object Login : Route("users/new") {
        operator fun invoke() = route
    }

    data object Cinema : Route("cinemas/{cinema}") {
        val arguments = listOf(
            navArgument("cinema") {
                type = NavType.StringType
            }
        )

        class Arguments(private val handle: SavedStateHandle) {
            val cinema get() = handle.get<String>("cinema").let(::requireNotNull)
        }

        operator fun invoke(cinema: String) =
            route.replace("{cinema}", cinema)

        fun deepLink(cinema: String) =
            "$InternalUri/${invoke(cinema)}".toUri()
    }

    data object Movie : Route("movies/{movie}?upcoming={upcoming}") {
        val arguments = listOf(
            navArgument("movie") {
                type = NavType.StringType
            },
            navArgument("upcoming") {
                type = NavType.BoolType
            }
        )

        class Arguments(private val entry: SavedStateHandle) {
            val movie get() = entry.get<String>("movie").let(::requireNotNull)
            val upcoming get() = entry.get<Boolean>("upcoming").let(::requireNotNull)
        }

        operator fun invoke(movie: String, upcoming: Boolean = false) =
            route.replace("{movie}", movie).replace("{upcoming}", upcoming.toString())

        fun deepLink(movie: String, upcoming: Boolean = false) =
            "$InternalUri/${invoke(movie, upcoming)}".toUri()
    }

    data object OrderComplete : Route("orders/success") {
        operator fun invoke() = route
    }

    data object Order : Route("orders/{url}") {
        val arguments = listOf(
            navArgument("url") {
                type = NavType.StringType
            }
        )

        class Arguments(private val entry: SavedStateHandle) {
            val url get() = entry.get<String>("url").let(::requireNotNull).decodeBase64()
        }

        operator fun invoke(url: String) =
            route.replace("{url}", url.encodeBase64())
    }

    data object Settings : Route("settings") {
        const val index = 4
        operator fun invoke() = route
    }

    data object Movies : Route("movies") {
        const val index = 1
        operator fun invoke() = route
    }

    data object Tickets : Route("tickets") {
        const val index = 3
        operator fun invoke() = route
    }

    data object Cinemas : Route("cinemas") {
        const val index = 2
        operator fun invoke() = route
    }

}
