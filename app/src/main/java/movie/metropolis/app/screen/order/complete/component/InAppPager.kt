package movie.metropolis.app.screen.order.complete.component

import android.app.Activity
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import androidx.compose.ui.unit.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import movie.metropolis.app.R
import movie.metropolis.app.model.ProductDetailView
import movie.metropolis.app.presentation.Loadable
import movie.style.AnticipateOvershootEasing
import movie.style.layout.PreviewLayout


@OptIn(ExperimentalPagerApi::class)
@Composable
fun InAppPager(
    items: Loadable<List<ProductDetailView>>,
    modifier: Modifier = Modifier,
    content: @Composable (ProductDetailView) -> Unit
) {
    AnimatedVisibility(
        visible = items.isSuccess,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(
                easing = AnticipateOvershootEasing,
                durationMillis = 600
            )
        ),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(
                easing = AnticipateOvershootEasing,
                durationMillis = 600
            )
        )
    ) {
        val items = items.getOrNull().orEmpty()
        HorizontalPager(
            modifier = modifier,
            count = 3,
            state = rememberPagerState(1),
            contentPadding = PaddingValues(start = 32.dp, end = 32.dp, top = 32.dp, bottom = 32.dp),
            verticalAlignment = Alignment.Top
        ) { index ->
            val item = items.getOrNull(index)
            if (item != null)
                content(item)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview(
    @PreviewParameter(ProductDetailViewPreview::class, 1)
    preview: ProductDetailView
) = PreviewLayout {
    InAppPager(
        modifier = Modifier.fillMaxWidth(),
        items = Loadable.success(List(3) { preview })
    ) {
        InAppPurchase(
            modifier = Modifier.padding(8.dp),
            icon = it.icon,
            title = it.name,
            description = it.description,
            price = it.price,
            isPopular = it.isPopular,
            onClick = {}
        )
    }
}

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