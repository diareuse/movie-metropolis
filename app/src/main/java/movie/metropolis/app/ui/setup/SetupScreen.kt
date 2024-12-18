package movie.metropolis.app.ui.setup

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import movie.metropolis.app.ui.setup.component.CountryRow
import movie.style.layout.PreviewLayout
import movie.style.util.pc

@Composable
fun SetupScreen(
    state: SetupViewState,
    onLoginClick: () -> Unit,
    onExitClick: () -> Unit,
    modifier: Modifier = Modifier,
) = Scaffold(modifier = modifier) { padding ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(1.pc, Alignment.CenterVertically)
    ) {
        for (region in state.regions) {
            CountryRow(
                selected = region == state.region,
                onClick = { state.region = region },
                icon = { Image(painterResource(region.icon), null) },
                label = { Text(region.name) }
            )
        }
        TextField(
            value = state.email,
            onValueChange = { state.email = it },
            placeholder = { Text("Email") },
            isError = state.error
        )
        TextField(
            value = state.password,
            onValueChange = { state.password = it },
            placeholder = { Text("Password") },
            isError = state.error
        )
        Button(onClick = onLoginClick, enabled = state.loginEnabled) {
            Text("Login")
        }
        Button(onClick = onExitClick, enabled = state.exitEnabled) {
            Text("Continue without logging in")
        }
    }
}

@PreviewLightDark
@PreviewFontScale
@Composable
private fun SetupScreenPreview() = PreviewLayout {
    SetupScreen(remember { SetupViewState() }, {}, {})
}