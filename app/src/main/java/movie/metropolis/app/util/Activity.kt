package movie.metropolis.app.util

import android.content.Context
import android.content.Intent
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import java.io.File

fun File.share(context: Context) {
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.PROVIDER", this, name)
    val intent = Intent(Intent.ACTION_SEND)
        .putExtra(Intent.EXTRA_STREAM, uri)
        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        .setType(MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension))
        .let { Intent.createChooser(it, name) }
    context.startActivity(intent)
}