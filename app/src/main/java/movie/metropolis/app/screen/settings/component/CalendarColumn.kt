@file:OptIn(ExperimentalFoundationApi::class)

package movie.metropolis.app.screen.settings.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.res.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import androidx.compose.ui.unit.*
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap
import movie.metropolis.app.R
import movie.metropolis.app.model.CalendarView
import movie.metropolis.app.screen.profile.component.ProfileIcon
import movie.style.layout.PreviewLayout
import movie.style.modifier.glow
import movie.style.theme.Theme
import java.util.Locale
import kotlin.random.Random.Default.nextLong

@Composable
fun CalendarColumn(
    calendars: ImmutableMap<String, List<CalendarView>>,
    onCalendarSelect: (String?) -> Unit,
    modifier: Modifier = Modifier,
) = LazyColumn(
    modifier = modifier,
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    for ((account, views) in calendars) {
        stickyHeader {
            AccountHeader(account, modifier = Modifier.padding(top = 16.dp))
        }
        items(views, key = { it.id }) {
            CalendarRow(
                label = { Text(it.name) },
                onClick = { onCalendarSelect(it.id) }
            )
        }
    }
    if (calendars.isEmpty()) item {
        // fixme replace with stringres
        Text("Couldn't find any calendars attached to this device.")
    }
    else item {
        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onCalendarSelect(null) }
        ) {
            // fixme replace with stringres
            Text("Close")
        }
    }
}

@Composable
private fun CalendarRow(
    label: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) = Row(
    modifier = modifier
        .fillMaxWidth()
        .clickable(role = Role.Button, onClick = onClick)
        .glow(Theme.container.button)
        .clip(Theme.container.button)
        .padding(horizontal = 16.dp)
        .minimumInteractiveComponentSize(),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    Icon(
        modifier = Modifier.size(16.dp),
        painter = painterResource(id = R.drawable.ic_calendar),
        contentDescription = null
    )
    ProvideTextStyle(Theme.textStyle.body) {
        label()
    }
}

@Composable
private fun AccountHeader(
    email: String,
    modifier: Modifier = Modifier,
) = Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    ProfileIcon(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape),
        email = email
    )
    Text(email, style = Theme.textStyle.emphasis)
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun CalendarColumnPreview() = PreviewLayout {
    val items = CalendarViewParameter(2).values.toList().groupBy { it.account }.toImmutableMap()
    CalendarColumn(calendars = items, {})
}

class CalendarViewParameter(
    override val count: Int
) : PreviewParameterProvider<CalendarView> {

    constructor() : this(count = 10)

    private val lorem = LoremIpsum(10)

    override val values = sequence {
        repeat(count) { yield(Data()) }
    }

    inner class Data(
        override val id: String = nextLong().toString(),
        override val name: String = lorem.values.first().split(" ").random()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
        override val account: String = "john.doe@email.com"
    ) : CalendarView
}