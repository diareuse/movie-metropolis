package movie.metropolis.app.screen.order.complete

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import movie.metropolis.app.R
import movie.metropolis.app.model.ProductDetailView
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.screen.order.complete.component.InAppPager
import movie.metropolis.app.screen.order.complete.component.InAppPurchase
import movie.metropolis.app.screen.order.complete.component.ProductDetailViewPreview
import movie.metropolis.app.screen.setup.component.Background
import movie.metropolis.app.util.findActivity
import movie.style.AnticipateOvershootEasing
import movie.style.AppToolbar
import movie.style.theme.Theme

@Composable
fun OrderCompleteScreen(
    viewModel: OrderCompleteViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val products by viewModel.products.collectAsState()
    OrderCompleteScreen(items = products, onBackClick = onBackClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OrderCompleteScreen(
    items: Loadable<List<ProductDetailView>>,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            AppToolbar(
                title = { Text(stringResource(R.string.order_complete_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(painterResource(id = R.drawable.ic_close), null)
                    }
                })
        }
    ) { padding ->
        Background(
            modifier = Modifier
                .alpha(.2f)
                .fillMaxSize(),
            count = 20
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxSize(),
                painter = painterResource(id = R.drawable.ic_favorite_checked),
                contentDescription = null,
                tint = when {
                    it % 3 == 0 -> Theme.color.container.secondary
                    it % 4 == 0 -> Theme.color.container.tertiary
                    else -> Theme.color.container.primary
                }
            )
        }
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .size(128.dp),
                painter = painterResource(R.drawable.ic_check_mark),
                contentDescription = null,
                tint = Theme.color.container.primary
            )
            Text(
                text = stringResource(R.string.order_complete_message),
                style = Theme.textStyle.body,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp),
                textAlign = TextAlign.Center
            )

            if (items.isSuccess)
                Divider(Modifier.padding(horizontal = 32.dp, vertical = 24.dp))

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
                Text(
                    text = stringResource(R.string.support_title),
                    style = Theme.textStyle.emphasis,
                    modifier = Modifier.padding(horizontal = 24.dp),
                    fontWeight = FontWeight.Bold
                )
            }
            val context = LocalContext.current
            val activity = remember(context) { context.findActivity() }
            val scope = rememberCoroutineScope()
            InAppPager(
                modifier = Modifier.systemGestureExclusion(),
                items = items
            ) {
                InAppPurchase(
                    modifier = Modifier.padding(8.dp),
                    icon = it.icon,
                    title = it.name,
                    description = it.description,
                    price = it.price,
                    isPopular = it.isPopular,
                    onClick = {
                        scope.launch {
                            it.purchase(activity)
                        }
                    }
                )
            }
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
                Text(
                    text = stringResource(R.string.support_bottom_line_caption),
                    style = Theme.textStyle.caption,
                    modifier = Modifier.padding(horizontal = 24.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview(
    @PreviewParameter(ProductDetailViewPreview::class, 1)
    preview: ProductDetailView
) = Theme {
    OrderCompleteScreen(
        items = Loadable.success(List(3) { preview }),
        onBackClick = {}
    )
}