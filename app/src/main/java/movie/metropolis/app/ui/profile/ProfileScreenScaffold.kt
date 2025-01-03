package movie.metropolis.app.ui.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.nestedscroll.*
import androidx.compose.ui.tooling.preview.*
import movie.style.layout.PreviewLayout
import movie.style.util.pc

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenScaffold(
    firstName: @Composable () -> Unit,
    lastName: @Composable () -> Unit,
    phone: @Composable () -> Unit,
    cinema: @Composable () -> Unit,
    consent: @Composable () -> Unit,
    card: @Composable () -> Unit,
    title: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
) = Scaffold(
    modifier = modifier,
    topBar = {
        LargeTopAppBar(
            modifier = Modifier
                .windowInsetsPadding(
                    WindowInsets.systemBars.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
                )
                .padding(1.pc)
                .clip(MaterialTheme.shapes.medium),
            windowInsets = WindowInsets(0),
            scrollBehavior = scrollBehavior,
            title = title,
            navigationIcon = navigationIcon
        )
    }
) { padding ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .verticalScroll(rememberScrollState())
            .padding(padding)
            .padding(1.pc),
        verticalArrangement = Arrangement.spacedBy(1.pc)
    ) {
        card()
        firstName()
        lastName()
        phone()
        cinema()
        consent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@PreviewFontScale
@Composable
private fun ProfileScreenScaffoldPreview() = PreviewLayout {
    ProfileScreenScaffold(
        firstName = { TextField("", {}) },
        lastName = { TextField("", {}) },
        phone = { TextField("", {}) },
        cinema = { TextField("", {}, readOnly = true) },
        consent = { Checkbox(true, {}) },
        card = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.4f)
                    .background(Color.Blue)
            )
        },
        title = { Text("Profile") },
        navigationIcon = { IconButton({}) { Icon(Icons.AutoMirrored.Default.ArrowBack, null) } },
    )
}