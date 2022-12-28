package movie.image

data class SwatchColor(
    val rgb: Int
) {

    companion object {

        val Black get() = SwatchColor(0xFF000000.toInt())
        val White get() = SwatchColor(0xFFFFFFFF.toInt())

    }

}