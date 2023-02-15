package movie.metropolis.app.model

import android.app.Activity

interface ProductDetailView {
    val icon: Int
    val name: String
    val description: String
    val price: String
    val isPopular: Boolean
    suspend fun purchase(activity: Activity)
}