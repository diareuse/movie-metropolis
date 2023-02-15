package movie.metropolis.app.model.adapter

import android.app.Activity
import movie.metropolis.app.R
import movie.metropolis.app.feature.billing.ProductDetail
import movie.metropolis.app.model.ProductDetailView

class ProductDetailViewFromFeature(
    private val detail: ProductDetail
) : ProductDetailView {

    override val icon: Int
        get() = when (detail.product.id) {
            "consumable.mini" -> R.drawable.ic_thumbs_up
            "consumable.medium" -> R.drawable.ic_drink
            "consumable.large" -> R.drawable.ic_popcorn
            else -> R.drawable.ic_eye
        }
    override val name: String
        get() = detail.name
    override val description: String
        get() = detail.description
    override val price: String
        get() = detail.price
    override val isPopular: Boolean
        get() = detail.product.id == "consumable.medium"

    override suspend fun purchase(activity: Activity) {
        detail.purchase(activity)
    }

}