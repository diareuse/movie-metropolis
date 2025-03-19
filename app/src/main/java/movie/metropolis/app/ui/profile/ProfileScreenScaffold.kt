package movie.metropolis.app.ui.profile

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.focus.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.nestedscroll.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.LocalHazeStyle
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import movie.metropolis.app.ui.profile.component.TransparentTextField
import movie.style.layout.PreviewLayout
import movie.style.util.pc

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun ProfileScreenScaffold(
    firstName: @Composable () -> Unit,
    lastName: @Composable () -> Unit,
    phone: @Composable () -> Unit,
    cinema: @Composable () -> Unit,
    consent: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
    haze: HazeState = remember { HazeState() }
) = Scaffold(
    modifier = modifier,
    topBar = {
        Row(
            modifier = Modifier
                .padding(2.pc)
                .statusBarsPadding()
        ) {
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .hazeEffect(haze)
            ) {
                navigationIcon()
            }
        }
    }
) { padding ->
    Box {
        val color = MaterialTheme.colorScheme.background
        Box(
            modifier = Modifier
                .blur(12.dp)
                .hazeSource(haze)
                .matchParentSize()
                .drawWithCache {
                    val brush = Brush.radialGradient(
                        colors = listOf(Color.Transparent, color),
                        center = Offset.Zero,
                        radius = size.height
                    )
                    onDrawWithContent {
                        drawContent()
                        drawRect(brush)
                    }
                },
            propagateMinConstraints = true
        ) {
            icon()
        }
        CompositionLocalProvider(
            LocalHazeStyle provides LocalHazeStyle.current.copy(
                blurRadius = 48.dp,
                noiseFactor = .1f
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .verticalScroll(rememberScrollState())
                    .imePadding()
                    .navigationBarsPadding()
                    .padding(padding)
                    .padding(2.pc),
                verticalArrangement = Arrangement.spacedBy(0.pc, Alignment.Bottom)
            ) {
                val ime = WindowInsets.ime.getBottom(LocalDensity.current) > 0
                var visible by remember { mutableIntStateOf(-1) }
                AnimatedVisibility(!ime || visible == -1 || visible == 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .zIndex(if (visible == 0) 5f else 0f)
                            .onFocusChanged {
                                visible =
                                    if (it.isFocused) 0 else if (!it.isFocused && visible == 0) -1 else visible
                            }
                            .clip(MaterialTheme.shapes.medium)
                            .hazeEffect(haze)
                            .padding(top = .5.pc),
                        propagateMinConstraints = true
                    ) {
                        firstName()
                    }
                }
                AnimatedVisibility(!ime) { Spacer(Modifier.height(1.pc)) }
                AnimatedVisibility(!ime || visible == -1 || visible == 1) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .zIndex(if (visible == 1) 5f else 0f)
                            .onFocusChanged {
                                visible =
                                    if (it.isFocused) 1 else if (!it.isFocused && visible == 1) -1 else visible
                            }
                            .clip(MaterialTheme.shapes.medium)
                            .hazeEffect(haze)
                            .padding(top = .5.pc),
                        propagateMinConstraints = true
                    ) {
                        lastName()
                    }
                }
                AnimatedVisibility(!ime) { Spacer(Modifier.height(1.pc)) }
                AnimatedVisibility(!ime || visible == -1 || visible == 2) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .zIndex(if (visible == 2) 5f else 0f)
                            .onFocusChanged {
                                visible =
                                    if (it.isFocused) 2 else if (!it.isFocused && visible == 2) -1 else visible
                            }
                            .clip(MaterialTheme.shapes.medium)
                            .hazeEffect(haze)
                            .padding(top = .5.pc),
                        propagateMinConstraints = true
                    ) {
                        phone()
                    }
                }
                AnimatedVisibility(!ime) { Spacer(Modifier.height(1.pc)) }
                AnimatedVisibility(!ime || visible == -1) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                            .hazeEffect(haze)
                            .padding(top = .5.pc),
                        propagateMinConstraints = true
                    ) {
                        cinema()
                    }
                }
                AnimatedVisibility(!ime) { Spacer(Modifier.height(1.pc)) }
                AnimatedVisibility(!ime || visible == -1) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                            .hazeEffect(haze)
                            .padding(top = .5.pc),
                        propagateMinConstraints = true
                    ) {
                        consent()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@PreviewFontScale
@Composable
private fun ProfileScreenScaffoldPreview() = PreviewLayout {
    ProfileScreenScaffold(
        icon = { Box(Modifier.background(Color.Blue)) },
        firstName = { TransparentTextField("", {}) },
        lastName = { TransparentTextField("", {}) },
        phone = { TransparentTextField("", {}) },
        cinema = { TransparentTextField("", {}, readOnly = true) },
        consent = { Checkbox(true, {}) },
        navigationIcon = { IconButton({}) { Icon(Icons.AutoMirrored.Default.ArrowBack, null) } },
    )
}