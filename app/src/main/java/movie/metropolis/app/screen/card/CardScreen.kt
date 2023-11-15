package movie.metropolis.app.screen.card

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import com.google.zxing.BarcodeFormat
import kotlinx.coroutines.launch
import movie.metropolis.app.R
import movie.metropolis.app.model.CinemaSimpleView
import movie.metropolis.app.model.MembershipView
import movie.metropolis.app.model.UserView
import movie.metropolis.app.screen.card.component.CardContentBack
import movie.metropolis.app.screen.card.component.CardContentFront
import movie.metropolis.app.screen.card.component.FlippableCard
import movie.metropolis.app.screen.setup.component.MembershipViewParameter
import movie.style.Barcode
import movie.style.layout.PreviewLayout
import movie.style.layout.alignForLargeScreen
import movie.style.modifier.glow
import movie.style.modifier.screenBrightness
import movie.style.modifier.surface
import movie.style.modifier.vertical
import movie.style.theme.Theme

private val CinemaCityColor = Color(0xFFE78838)

@Composable
fun CardScreen(
    user: UserView,
    membership: MembershipView,
    onCloseRequest: () -> Unit,
    modifier: Modifier = Modifier,
    state: CardScreenState = remember { CardScreenState() },
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    BackHandler {
        onCloseRequest()
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(state.background)
            .onSizeChanged { state.updateOffset(with(density) { it.height.toDp() }) }
            .pointerInput(Unit) {
                scope.launch {
                    detectHorizontalDragGestures(
                        onDragCancel = { scope.launch { state.settle() } },
                        onDragEnd = { scope.launch { state.settle() } }
                    ) { change, amount ->
                        change.consume()
                        val amount = if (amount < 0) 360 + amount else amount
                        state.rotation += amount
                        if (state.rotation > 360f)
                            state.rotation -= 360f
                    }
                }
                scope.launch {
                    detectTapGestures {
                        onCloseRequest()
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        val logo = @Composable {
            Image(
                painter = painterResource(id = R.drawable.ic_logo_cinemacity),
                contentDescription = null,
                colorFilter = ColorFilter.tint(CinemaCityColor),
                contentScale = ContentScale.FillHeight
            )
        }
        val name = @Composable {
            Text("CLUB")
        }
        FlippableCard(
            modifier = Modifier
                .offset(y = state.offset)
                .fillMaxWidth()
                .alignForLargeScreen(400.dp)
                .vertical(),
            rotation = state.rotation,
            key = membership.cardNumber,
            container = {
                val shape = RoundedCornerShape(16.dp)
                Box(
                    modifier = Modifier
                        .surface(
                            color = Color.Black,
                            shape = shape,
                            elevation = 16.dp,
                            shadowColor = CinemaCityColor
                        )
                        .glow(shape, Color.Black),
                    propagateMinConstraints = true
                ) {
                    CompositionLocalProvider(LocalContentColor provides Color.White) {
                        it()
                    }
                }
            },
            front = {
                CardContentFront(logo = logo, name = name, cardholder = {
                    CompositionLocalProvider(
                        LocalContentColor provides LocalContentColor.current.copy(alpha = .7f)
                    ) {
                        Text(stringResource(id = R.string.points_count, membership.points))
                        when {
                            membership.isExpired -> Text(stringResource(R.string.expired))
                            else -> Text(
                                stringResource(R.string.expires_at, membership.daysRemaining)
                            )
                        }

                    }
                    Text(
                        "%s %s".format(user.firstName, user.lastName),
                        style = Theme.textStyle.title
                    )
                })
            },
            back = {
                CardContentBack(logo = logo, name = name, code = {
                    var fullBrightness by remember { mutableStateOf(false) }
                    Barcode(
                        modifier = Modifier
                            .surface(Color.White, Theme.container.poster)
                            .padding(vertical = 8.dp)
                            .screenBrightness(full = fullBrightness)
                            .clickable { fullBrightness = !fullBrightness },
                        code = membership.cardNumber,
                        format = BarcodeFormat.CODE_128
                    )
                })
            }
        )
    }
}

@Preview
@Composable
private fun CardScreenPreview() = PreviewLayout {
    val membership = remember { MembershipViewParameter().values.first() }
    val user = remember { UserViewParameter().values.first() }
    val state = remember { CardScreenState().also { it.updateOffset(0.dp) } }
    CardScreen(user, membership, state = state, onCloseRequest = {})
}

@Preview
@Composable
private fun CardScreenBackPreview() = PreviewLayout {
    val membership = remember { MembershipViewParameter().values.first() }
    val user = remember { UserViewParameter().values.first() }
    val state = remember {
        CardScreenState().also {
            it.updateOffset(0.dp)
            it.rotation = 180f
        }
    }
    CardScreen(user, membership, state = state, onCloseRequest = {})
}

class UserViewParameter : PreviewParameterProvider<UserView> {
    override val values = sequence<UserView> {
        yield(Data())
    }

    private data class Data(
        override val firstName: String = "John",
        override val lastName: String = "Doe",
        override val email: String = "joh.doe@email.com",
        override val phone: String = "+1 234 2344233",
        override val favorite: CinemaSimpleView? = null,
        override val consent: UserView.ConsentView = Consent()
    ) : UserView

    private data class Consent(
        override val marketing: Boolean = false,
        override val premium: Boolean = false
    ) : UserView.ConsentView
}