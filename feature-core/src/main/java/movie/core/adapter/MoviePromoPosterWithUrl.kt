package movie.core.adapter

import movie.core.model.MoviePromoPoster
import movie.core.nwk.EndpointProvider

data class MoviePromoPosterWithUrl(
    private val origin: MoviePromoPoster,
    private val provider: EndpointProvider
) : MoviePromoPoster by origin {

    override val url: String
        get() = "${provider.domain}/magnoliaPublic/dam/${origin.url}"

}