@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package movie.metropolis.app.screen.purchase

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import movie.metropolis.app.R
import movie.metropolis.app.model.ProductDetailView
import movie.metropolis.app.screen.purchase.component.InAppColumn
import movie.metropolis.app.screen.purchase.component.ProductDetailViewPreview
import movie.metropolis.app.util.interpolatePage
import movie.style.CollapsingTopAppBar
import movie.style.layout.PreviewLayout
import movie.style.layout.alignForLargeScreen
import movie.style.theme.Theme
import movie.style.util.findActivity

@Composable
fun PurchaseCompleteScreen(
    products: ImmutableList<ProductDetailView>,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) = Scaffold(
    modifier = modifier,
    topBar = {
        CollapsingTopAppBar(
            modifier = Modifier.alignForLargeScreen(),
            title = { Text(stringResource(R.string.order_complete_title)) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(painterResource(R.drawable.ic_close), null)
                }
            }
        )
    }
) { padding ->
    BackHandler {
        onBackClick()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(padding)
            .padding(vertical = 24.dp)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(fontSize = 128.sp, text = "🎉")
        Text(
            text = stringResource(R.string.order_complete_message),
            style = Theme.textStyle.body,
            modifier = Modifier.padding(horizontal = 24.dp),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = stringResource(R.string.support_title),
            style = Theme.textStyle.emphasis,
            modifier = Modifier.padding(horizontal = 24.dp),
            fontWeight = FontWeight.Bold
        )
        val pagerState = rememberPagerState(1) { products.size }
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 64.dp)
        ) {
            val item = products[it]
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .interpolatePage(
                        state = pagerState,
                        page = it,
                        blur = 8.dp,
                        rotationY = 15f,
                        rotationZ = 0f,
                        offset = DpOffset.Zero
                    )
            ) {
                InAppColumn(
                    modifier = Modifier.fillMaxWidth(),
                    icon = { Icon(painterResource(item.icon), null) },
                    name = { Text(item.name) },
                    description = { Text(item.description) },
                    price = { Text(item.price) },
                    onClick = {
                        scope.launch {
                            item.purchase(context.findActivity())
                        }
                    }
                )
            }
        }
        Spacer(Modifier.weight(1f))
        Text(
            text = stringResource(R.string.support_bottom_line_caption),
            style = Theme.textStyle.caption,
            modifier = Modifier.padding(horizontal = 24.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun PurchaseCompleteScreenPreview() = PreviewLayout {
    val products = ProductDetailViewPreview().values.toList()
    PurchaseCompleteScreen(
        products = products.toImmutableList(),
        onBackClick = {}
    )
}