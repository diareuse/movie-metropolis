package movie.metropolis.app.screen

import androidx.compose.runtime.*
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import movie.metropolis.app.screen.home.HomeState
import movie.metropolis.app.screen.setup.SetupState
import movie.style.util.decodeBase64
import movie.style.util.encodeBase64

@Immutable
sealed class Route(
    val route: String
) {

    companion object {

        private const val InternalUri = "app://movie.metropolis"

    }

    open val deepLinks = listOf(
        navDeepLink { uriPattern = "$InternalUri/$route" }
    )

    data object None : Route("null")

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

    data object UserEditor : Route("users/me/edit") {
        operator fun invoke() = route
    }

    data object User : Route("users/me") {
        operator fun invoke() = route
    }

    data object Tickets : Route("users/me/tickets") {
        operator fun invoke() = route
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

                companion object {
                    val keys = listOf("movie")
                }
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

                companion object {
                    val keys = listOf("cinema")
                }
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
        operator fun invoke() = route
    }

}
