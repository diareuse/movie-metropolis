package movie.metropolis.app.screen.purchase.component

import android.app.Activity
import androidx.compose.ui.tooling.preview.datasource.*
import movie.metropolis.app.R
import movie.metropolis.app.model.ProductDetailView

class ProductDetailViewPreview : CollectionPreviewParameterProvider<ProductDetailView>(
    listOf(
        view(
            R.drawable.ic_thumbs_up,
            "Thumbs up",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent tristique dolor tellus, in tristique augue semper eget. Vestibulum cursus imperdiet tincidunt.",
            "€2.99"
        ),
        view(
            R.drawable.ic_drink,
            "Drink",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent tristique dolor tellus, in tristique augue semper eget. Vestibulum cursus imperdiet tincidunt.",
            "€7.99",
            true
        ),
        view(
            R.drawable.ic_popcorn,
            "Popcorn",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent tristique dolor tellus, in tristique augue semper eget. Vestibulum cursus imperdiet tincidunt.",
            "€12.99"
        ),
    )
) {

    companion object {

        private fun view(
            icon: Int,
            name: String,
            description: String,
            price: String,
            isPopular: Boolean = false
        ) = object : ProductDetailView {
            override val icon: Int = icon
            override val name: String = name
            override val description: String = description
            override val price: String = price
            override val isPopular: Boolean = isPopular
            override suspend fun purchase(activity: Activity) = Unit
        }

    }

}