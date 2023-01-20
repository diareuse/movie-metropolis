package movie.core

import movie.core.model.Cinema
import movie.core.model.MovieReference
import movie.core.model.Showing

typealias ResultCallback<T> = suspend (Result<T>) -> Unit
typealias MovieWithShowings = Map<MovieReference, Iterable<Showing>>
typealias CinemaWithShowings = Map<Cinema, Iterable<Showing>>