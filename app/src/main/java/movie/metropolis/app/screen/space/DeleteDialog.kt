package movie.metropolis.app.screen.space

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.model.DeleteProgress
import movie.style.Container
import movie.style.DialogScope
import movie.style.layout.PreviewLayout
import movie.style.theme.Theme

@Composable
fun DeleteDialog(
    progress: DeleteProgress?,
    modifier: Modifier = Modifier,
) = Box(modifier = modifier, propagateMinConstraints = true) {
    if (progress != null) {
        val progress by animateFloatAsState(targetValue = progress.progress)
        LinearProgressIndicator(
            modifier = Modifier
                .matchParentSize()
                .alpha(.2f),
            progress = { progress }
        )
    }
    Column(modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Deleting files", style = Theme.textStyle.title)
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                strokeWidth = 3.dp,
                strokeCap = StrokeCap.Round,
            )
        }

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            visible = progress != null
        ) {
            Text(
                "%d out of %d deleted".format(progress?.deleted ?: 0, progress?.max ?: 0),
                style = Theme.textStyle.caption
            )
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun DeleteDialogPreview() = PreviewLayout {
    val s = object : DialogScope {}
    s.Container(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = { /*TODO*/ }) {
        DeleteDialog(DeleteProgress(10, 1000))
    }
}