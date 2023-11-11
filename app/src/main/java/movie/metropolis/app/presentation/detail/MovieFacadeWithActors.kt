package movie.metropolis.app.presentation.detail

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.PersonView
import movie.rating.Actor
import movie.rating.ActorProvider

class MovieFacadeWithActors(
    private val origin: MovieFacade,
    provider: ActorProvider
) : MovieFacade by origin {

    private val actors = ActorProviderCaching(provider)

    override val movie: Flow<MovieDetailView> = origin.movie.flatMapLatest {
        channelFlow {
            val it = MovieDetailReplacing(it)
            send(it)
            val directors = it.directors.toMutableList()
            val cast = it.cast.toMutableList()
            val directorsMutex = Mutex()
            val castMutex = Mutex()
            for ((index, d) in directors.withIndex()) launch {
                val actor = actors.runCatching { get(d.name) }.getOrNull() ?: return@launch
                val out = directorsMutex.withLock {
                    directors[index] = PersonViewFromActor(actor)
                    directors.toList()
                }
                send(it.copy(directors = out))
            }
            for ((index, c) in cast.withIndex()) launch {
                val actor = actors.runCatching { get(c.name) }.getOrNull() ?: return@launch
                val out = castMutex.withLock {
                    cast[index] = PersonViewFromActor(actor)
                    cast.toList()
                }
                send(it.copy(cast = out))
            }
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

    data class MovieDetailReplacing(
        private val origin: MovieDetailView,
        override val directors: List<PersonView> = origin.directors,
        override val cast: List<PersonView> = origin.cast
    ) : MovieDetailView by origin

    data class PersonViewFromActor(
        private val actor: Actor
    ) : PersonView {
        override val name: String
            get() = actor.name
        override val popularity: Int
            get() = actor.popularity
        override val image: String
            get() = actor.image
        override val starredInMovies: Int
            get() = actor.movies.size
    }

}