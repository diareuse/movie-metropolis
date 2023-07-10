package movie.style

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.gestures.snapping.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.*
import androidx.compose.material3.windowsizeclass.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.input.nestedscroll.*
import androidx.compose.ui.unit.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppHorizontalPager(
    state: PagerState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    size: WindowSizeClass = LocalWindowSizeClass.current,
    beyondBoundsPageCount: Int = 0,
    pageSpacing: Dp = 0.dp,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    flingBehavior: SnapFlingBehavior = PagerDefaults.flingBehavior(state = state),
    userScrollEnabled: Boolean = true,
    reverseLayout: Boolean = false,
    key: ((index: Int) -> Any)? = null,
    pageNestedScrollConnection: NestedScrollConnection = PagerDefaults.pageNestedScrollConnection(
        state,
        Orientation.Horizontal
    ),
    pageContent: @Composable PagerScope.(page: Int) -> Unit
) = HorizontalPager(
    state = state,
    modifier = modifier,
    contentPadding = contentPadding,
    pageSize = when (size.widthSizeClass) {
        WindowWidthSizeClass.Compact -> PageSize.Fill
        else -> PageSize.Fixed(400.dp)
    },
    beyondBoundsPageCount = beyondBoundsPageCount,
    pageSpacing = pageSpacing,
    verticalAlignment = verticalAlignment,
    flingBehavior = flingBehavior,
    userScrollEnabled = userScrollEnabled,
    reverseLayout = reverseLayout,
    key = key,
    pageNestedScrollConnection = pageNestedScrollConnection,
    pageContent = pageContent
)