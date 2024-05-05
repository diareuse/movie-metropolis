package movie.cinema.city.adapter

import movie.cinema.city.PromoCard
import movie.cinema.city.model.PromoCardResponse
import java.net.URL

internal data class PromoCardFromResponse(
    private val card: PromoCardResponse
) : PromoCard {
    override val id: String
        get() = card.id
    override val title: String
        get() = card.title
    override val description: String
        get() = card.description
    override val image: URL
        get() = URL(card.image)
    override val url: URL?
        get() = card.url?.let(::URL)
}