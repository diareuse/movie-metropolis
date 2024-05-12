package movie.cinema.city

import movie.cinema.city.adapter.MovieFromDatabase
import movie.cinema.city.persistence.MovieDao
import movie.cinema.city.persistence.MovieStored

internal class CinemaCityStorage(
    client: CinemaCityClient,
    private val dao: MovieDao
) : CinemaCityComposition(client) {

    override val events: CinemaCity.Events by lazy { Events(super.events) }

    private inner class Events(
        private val origin: CinemaCity.Events
    ) : CinemaCity.Events by origin {
        override suspend fun getEvent(id: String): Movie = try {
            MovieFromDatabase(
                storedMovie = dao.selectMovie(id),
                storedCast = dao.selectCast(id),
                storedDirectors = dao.selectDirector(id),
                storedGenre = dao.selectGenre(id),
                storedImage = dao.selectImage(id),
                storedVideo = dao.selectVideo(id)
            )
        } catch (ignore: Throwable) {
            ignore.printStackTrace()
            origin.getEvent(id).also {
                dao.insert(MovieStored(it))
                val cast = it.cast
                    .map { i -> MovieStored.Cast(it.id, i) }
                val director = it.directors
                    .map { i -> MovieStored.Director(it.id, i) }
                val genre = it.genres
                    .map { i -> MovieStored.Genre(it.id, i) }
                val images = it.images
                    .map { i -> MovieStored.Image(it.id, i.width, i.height, i.url) }
                val video = it.videos
                    .map { i -> MovieStored.Video(it.id, i) }
                dao.insertCast(cast)
                dao.insertDirector(director)
                dao.insertGenre(genre)
                dao.insertImage(images)
                dao.insertVideo(video)
            }
        }
    }

}