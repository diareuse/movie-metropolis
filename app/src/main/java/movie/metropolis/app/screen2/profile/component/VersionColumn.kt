package movie.metropolis.app.screen2.profile.component

import android.content.pm.PackageInfo
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import movie.style.layout.PreviewLayout
import movie.style.theme.Theme

private val PackageInfo.versionCodeCompat get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) longVersionCode else versionCode.toLong()

@Composable
fun VersionColumn(
    modifier: Modifier = Modifier,
) = Column(modifier = modifier) {
    val context = LocalContext.current
    val info = remember(context) { context.packageManager.getPackageInfo(context.packageName, 0) }
    if (info != null) Text(
        text = "%s (%d)".format(info.versionName, info.versionCodeCompat),
        style = Theme.textStyle.caption.copy(
            textAlign = TextAlign.Center,
            color = LocalContentColor.current.copy(.5f)
        )
    )
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun VersionColumnPreview() = PreviewLayout {
    VersionColumn()
}
