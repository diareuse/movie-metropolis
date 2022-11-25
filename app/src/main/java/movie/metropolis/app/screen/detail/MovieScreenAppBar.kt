package movie.metropolis.app.screen.detail

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import movie.metropolis.app.R
import movie.metropolis.app.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreenAppBar(
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    onBackClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent),
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(painterResource(id = R.drawable.ic_back), null)
            }
        },
        title = { Text("Movie Detail") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        MovieScreenAppBar(onBackClick = {})
    }
}