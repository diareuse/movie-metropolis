package movie.core.adapter

import movie.core.db.model.MovieMediaStored
import movie.core.db.model.MovieMediaView
import movie.core.model.Media

fun MediaFromDatabase(media: MovieMediaView) = when (media.type) {
    MovieMediaStored.Type.Image -> Media.Image(media.width ?: 0, media.height ?: 0, media.url)
    MovieMediaStored.Type.Video -> Media.Video(media.url)
}