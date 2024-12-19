package movie.metropolis.app.presentation.detail

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.PersonView
import movie.metropolis.app.util.onEachLaunch
import movie.rating.Actor
import movie.rating.ActorProvider

class MovieFacadeWithActors(
    private val origin: MovieFacade,
    provider: ActorProvider
) : MovieFacade by origin {

    private val actors = ActorProviderCaching(provider)

    override val movie: Flow<MovieDetailView> = origin.movie.onEachLaunch {
        val directors = it.directors.toMutableList()
        val cast = it.cast.toMutableList()
        for (p in directors + cast) launch {
            val actor = actors.runCatching { get(p.name) }.getOrNull() ?: return@launch
            p.url = "https://www.themoviedb.org/person/${actor.id}"
            p.popularity = actor.popularity
            p.image = actor.image
            p.movies.clear()
            p.movies.addAll(actor.movies.map { a ->
                PersonView.Movie(
                    id = a.id,
                    name = a.name,
                    backdrop = a.backdrop,
                    image = a.image,
                    popularity = a.popularity,
                    rating = a.rating,
                    releasedAt = a.releasedAt
                )
            })
        }
    }

    class ActorProviderCaching(
        private val origin: ActorProvider
    ) : ActorProvider {
        private val cache = mutableMapOf<String, Actor>()
        private val mutex = Mutex()
        override suspend fun get(query: String): Actor {
            return cache[query] ?: mutex.withLock { cache[query] } ?: origin.get(query).also {
                mutex.withLock {
                    cache[query] = it
                }
            }
        }
    }

}