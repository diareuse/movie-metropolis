package movie.image

data class Swatch(
    val colors: List<SwatchColor>
) {

    val vibrant get() = colors.maxBy { it.saturation + it.value }

}