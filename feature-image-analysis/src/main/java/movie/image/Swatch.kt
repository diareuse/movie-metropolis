package movie.image

data class Swatch(
    val colors: List<SwatchColor>
) {

    val vibrant get() = colors.maxByOrNull { it.saturation + it.value } ?: SwatchColor(0x000000)

}