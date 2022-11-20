package movie.metropolis.app.feature.global

sealed class Media {

    data class Image(
        val width: Int,
        val height: Int,
        val url: String
    ) : Media() {

        val aspectRatio
            get() = 1f * width / height

    }

    data class Video(
        val url: String
    ) : Media()

}