package movie.metropolis.app.screen

import androidx.compose.runtime.*
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import movie.metropolis.app.screen2.home.HomeState
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
            UserEditor.route -> UserEditor
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

    data object Upcoming : Route("listing/{type}") {
        operator fun invoke() = route.replace("{type}", "upcoming")
    }

    data object Home : Route("home?screen={screen}") {
        val arguments = listOf(
            navArgument("screen") {
                type = NavType.StringType
                nullable = true
            }
        )

        class Arguments(private val entry: NavBackStackEntry) {
            val screen get() = entry.arguments?.getString("screen")
        }

        operator fun invoke(screen: HomeState? = null) = route.let {
            if (screen != null) it.replace("{screen}", screen.name) else
                it.replace("screen={screen}", "")
        }.let {
            if (it.endsWith("?")) it.substringBeforeLast('?') else it
        }

        fun deepLink(screen: HomeState = HomeState.Listing) =
            "$InternalUri/${invoke(screen)}".toUri()

    }

    data object UserEditor : Route("users/me") {
        operator fun invoke() = route
    }

    @Deprecated("")
    data object Login : Route("users/new") {
        operator fun invoke() = route
    }

    @Deprecated("")
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

        class Arguments(
            val movie: String,
            val upcoming: Boolean
        ) {
            constructor(entry: NavBackStackEntry) : this(
                entry.arguments?.getString("movie", null).let(::requireNotNull),
                entry.arguments?.getBoolean("upcoming", false).let(::requireNotNull)
            )

            constructor(handle: SavedStateHandle) : this(
                handle.get<String>("movie").let(::requireNotNull),
                handle.get<Boolean>("upcoming").let(::requireNotNull)
            )
        }

        operator fun invoke(movie: String, upcoming: Boolean = false) =
            route.replace("{movie}", movie).replace("{upcoming}", upcoming.toString())

        fun deepLink(movie: String, upcoming: Boolean = false) =
            "$InternalUri/${invoke(movie, upcoming)}".toUri()
    }

    sealed class Booking(route: String) : Route(route) {

        data object Movie : Booking("movies/{movie}/booking") {

            val arguments = listOf(
                navArgument("movie") {
                    type = NavType.StringType
                }
            )

            class Arguments(private val entry: SavedStateHandle) {
                val movie get() = entry.get<String>("movie").let(::requireNotNull)
            }

            operator fun invoke(movie: String) =
                route.replace("{movie}", movie)

            fun deepLink(movie: String) =
                "$InternalUri/${invoke(movie)}".toUri()

        }

        data object Cinema : Booking("cinemas/{cinema}/booking") {

            val arguments = listOf(
                navArgument("cinema") {
                    type = NavType.StringType
                }
            )

            class Arguments(private val entry: SavedStateHandle) {
                val cinema get() = entry.get<String>("cinema").let(::requireNotNull)
            }

            operator fun invoke(cinema: String) =
                route.replace("{cinema}", cinema)

            fun deepLink(cinema: String) =
                "$InternalUri/${invoke(cinema)}".toUri()

        }
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
