package movie.metropolis.app.screen

import androidx.compose.runtime.*
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import movie.metropolis.app.util.decodeBase64
import movie.metropolis.app.util.encodeBase64

@Immutable
sealed class Route(
    val route: String
) {

    companion object {

        private const val InternalUri = "app://movie.metropolis"
        fun by(name: String) = when (name) {
            Cinema() -> Cinema
            Cinemas() -> Cinemas
            Home() -> Home
            Login() -> Login
            Movie() -> Movie
            Movies() -> Movies
            Order() -> Order
            OrderComplete() -> OrderComplete
            Settings() -> Settings
            Setup() -> Setup
            Tickets() -> Tickets
            User() -> User
            else -> throw IllegalArgumentException("Unknown route name $name")
        }

    }

    operator fun invoke() = route

    open val deepLinks = listOf(
        navDeepLink { uriPattern = "$InternalUri/$route" }
    )

    object Setup : Route("setup") {
        fun destination() = route
    }

    object Home : Route("home?screen={screen}") {
        val arguments = listOf(
            navArgument("screen") {
                type = NavType.StringType
                defaultValue = Movies.destination()
            }
        )

        class Arguments(private val entry: NavBackStackEntry) {
            val screen get() = entry.arguments?.getString("screen").let(::requireNotNull)
        }

        fun destination(screen: String = Movies.destination()) =
            route.replace("{screen}", screen)

        fun deepLink(screen: String = Movies.destination()) =
            "$InternalUri/${destination(screen)}".toUri()

    }

    object User : Route("users/me") {
        fun destination() = route
    }

    object Login : Route("users/new") {
        fun destination() = route
    }

    object Cinema : Route("cinemas/{cinema}") {
        val arguments = listOf(
            navArgument("cinema") {
                type = NavType.StringType
            }
        )

        class Arguments(private val handle: SavedStateHandle) {
            val cinema get() = handle.get<String>("cinema").let(::requireNotNull)
        }

        fun destination(cinema: String) =
            route.replace("{cinema}", cinema)

        fun deepLink(cinema: String) =
            "$InternalUri/${destination(cinema)}".toUri()
    }

    object Movie : Route("movies/{movie}?upcoming={upcoming}") {
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

        fun destination(movie: String, upcoming: Boolean = false) =
            route.replace("{movie}", movie).replace("{upcoming}", upcoming.toString())

        fun deepLink(movie: String, upcoming: Boolean = false) =
            "$InternalUri/${destination(movie, upcoming)}".toUri()
    }

    object OrderComplete : Route("orders/success") {
        fun destination() = route
    }

    object Order : Route("orders/{url}") {
        val arguments = listOf(
            navArgument("url") {
                type = NavType.StringType
            }
        )

        class Arguments(private val entry: SavedStateHandle) {
            val url get() = entry.get<String>("url").let(::requireNotNull).decodeBase64()
        }

        fun destination(url: String) =
            route.replace("{url}", url.encodeBase64())
    }

    object Settings : Route("settings") {
        fun destination() = route
    }

    object Movies : Route("movies") {
        fun destination() = route
    }

    object Tickets : Route("tickets") {
        fun destination() = route
    }

    object Cinemas : Route("cinemas") {
        fun destination() = route
    }

}
