package movie.metropolis.app.screen.profile.component

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import androidx.compose.ui.unit.*
import com.google.zxing.BarcodeFormat
import movie.metropolis.app.R
import movie.metropolis.app.model.MembershipView
import movie.metropolis.app.screen.profile.component.MembershipCardParameter.Data
import movie.style.Barcode
import movie.style.layout.PreviewLayout

@Composable
fun MembershipCard(
    firstName: String,
    lastName: String,
    view: MembershipView,
    modifier: Modifier = Modifier,
) {
    MembershipCardLayout(
        modifier = modifier,
        name = { Text("%s %s".format(firstName, lastName)) },
        expiration = { Text(stringResource(R.string.expires_at, view.memberUntil)) },
        points = { Text(stringResource(R.string.points_count, view.points)) },
        barcode = {
            Barcode(
                modifier = Modifier.fillMaxSize(),
                code = view.cardNumber,
                format = BarcodeFormat.CODE_128,
                color = Color.Black
            )
        }
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun MembershipCardLayoutPreview(
    @PreviewParameter(MembershipCardParameter::class)
    parameter: Data
) = PreviewLayout {
    MembershipCard(
        firstName = parameter.firstName,
        lastName = parameter.lastName,
        view = parameter.view
    )
}

private class MembershipCardParameter : CollectionPreviewParameterProvider<Data>(listOf(Data())) {
    data class Data(
        val firstName: String = "Name",
        val lastName: String = "Surname",
        val view: MembershipView = MembershipViewParameter().values.first()
    )
}

class MembershipViewParameter : CollectionPreviewParameterProvider<MembershipView>(
    listOf(
        Data(),
        Data(isExpired = true)
    )
) {
    data class Data(
        override val isExpired: Boolean = false,
        override val cardNumber: String = "123456789",
        override val memberFrom: String = "Jan 1 2005",
        override val memberUntil: String = "Jan 1 2024",
        override val daysRemaining: String = "30 days",
        override val points: String = "100"
    ) : MembershipView
}