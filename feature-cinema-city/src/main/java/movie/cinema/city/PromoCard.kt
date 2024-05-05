package movie.cinema.city

import java.net.URL

interface PromoCard {
    val id: String
    val title: String
    val description: String
    val image: URL
    val url: URL?
}