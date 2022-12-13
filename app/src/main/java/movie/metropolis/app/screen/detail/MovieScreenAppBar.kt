package movie.metropolis.app.screen.detail

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import movie.metropolis.app.R
import movie.metropolis.app.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreenAppBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent),
        navigationIcon = {
            Surface(
                modifier = Modifier.padding(start = 12.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 1.dp,
                shadowElevation = 16.dp,
                shape = MaterialTheme.shapes.medium
            ) {
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(painterResource(id = R.drawable.ic_back), null)
                }
            }
        },
        title = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        MovieScreenAppBar(onBackClick = {})
    }
}