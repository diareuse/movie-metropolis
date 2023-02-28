package movie.metropolis.app.screen.order.complete.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.R
import movie.metropolis.app.model.ProductDetailView
import movie.style.haptic.withHaptics
import movie.style.layout.PreviewLayout
import movie.style.modifier.surface
import movie.style.theme.Theme


@Composable
fun InAppPurchase(
    icon: Int,
    title: String,
    description: String,
    price: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPopular: Boolean = false
) {
    Box(modifier = modifier, contentAlignment = Alignment.TopCenter) {
        var height by remember { mutableStateOf(0) }
        Column(
            Modifier
                .padding(top = 40.dp)
                .padding(bottom = with(LocalDensity.current) { height.toDp() / 2 })
                .surface(4.dp, shape = Theme.container.card)
                .clickable(onClick = onClick.withHaptics())
                .padding(top = 16.dp)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(title, style = Theme.textStyle.title, textAlign = TextAlign.Center)
            Text(description, style = Theme.textStyle.caption, textAlign = TextAlign.Center)
        }
        Row(
            modifier = Modifier
                .surface(
                    Theme.color.container.primary,
                    shape = CircleShape,
                    elevation = 16.dp,
                    shadowColor = Theme.color.container.primary
                )
                .clickable(onClick = onClick.withHaptics())
                .padding(end = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(80.dp)
                    .padding(16.dp),
                painter = painterResource(icon),
                contentDescription = null,
                tint = Theme.color.content.primary
            )
            Text(price, style = Theme.textStyle.title)
        }
        if (isPopular) Row(
            modifier = Modifier
                .onGloballyPositioned { height = it.size.height }
                .align(Alignment.BottomCenter)
                .surface(
                    Theme.color.container.tertiary,
                    shape = CircleShape,
                    elevation = 8.dp,
                    shadowColor = Theme.color.container.tertiary
                )
                .padding(16.dp, 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painterResource(R.drawable.ic_tada), null, tint = Theme.color.content.tertiary)
            Text("Most popular", color = Theme.color.content.tertiary)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview(
    @PreviewParameter(ProductDetailViewPreview::class, 3)
    preview: ProductDetailView
) = PreviewLayout {
    InAppPurchase(
        icon = preview.icon,
        title = preview.name,
        description = preview.description,
        price = preview.price,
        isPopular = preview.name == "Drink",
        onClick = {}
    )
}