package movie.metropolis.app.util

import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import movie.metropolis.app.model.facade.FormatConfig

fun MultiFormatWriter.prepare(
    width: Int,
    height: Int,
    format: BarcodeFormat
) = FormatConfig(
    writer = this,
    width = width,
    height = height,
    format = format
)