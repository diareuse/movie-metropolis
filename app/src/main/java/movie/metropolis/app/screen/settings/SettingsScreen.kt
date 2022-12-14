package movie.metropolis.app.screen.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import movie.metropolis.app.R
import movie.metropolis.app.screen.detail.plus
import movie.metropolis.app.theme.Theme

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val filterSeen by viewModel.filterSeen.collectAsState()
    SettingsScreen(
        filterSeen = filterSeen,
        onFilterSeenChanged = viewModel::updateFilterSeen,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    filterSeen: Boolean,
    onFilterSeenChanged: (Boolean) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(painterResource(id = R.drawable.ic_back), null)
                    }
                },
                title = {
                    Text("Settings")
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = padding + PaddingValues(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            FilterSeen(
                checked = filterSeen,
                onCheckedChanged = onFilterSeenChanged
            )
            item("notice") {
                Text(
                    modifier = Modifier.navigationBarsPadding(),
                    text = "Some settings may require for you to close the app completely and start again to apply.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Suppress("FunctionName")
fun LazyListScope.FilterSeen(
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit
) = item("filter-seen") {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable { onCheckedChanged(!checked) }
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChanged
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Show unseen only",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "If checked, app will show only movies that are not present in the Tickets section.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        SettingsScreen(
            filterSeen = true,
            onFilterSeenChanged = {},
            onBackClick = {}
        )
    }
}