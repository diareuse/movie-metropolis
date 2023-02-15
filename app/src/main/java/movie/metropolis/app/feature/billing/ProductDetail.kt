package movie.metropolis.app.feature.billing

import android.app.Activity

interface ProductDetail {

    val product: Product
    val name: String
    val title: String
    val description: String
    val price: String

    suspend fun purchase(activity: Activity)

}