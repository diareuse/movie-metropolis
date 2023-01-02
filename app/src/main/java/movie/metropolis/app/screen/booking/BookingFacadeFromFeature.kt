package movie.metropolis.app.screen.booking

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import movie.core.TicketShareRegistry
import movie.core.UserFeature
import movie.core.model.Booking
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.adapter.BookingViewActiveFromFeature
import movie.metropolis.app.model.adapter.BookingViewExpiredFromFeature
import java.io.File

class BookingFacadeFromFeature(
    private val feature: UserFeature,
    private val online: UserFeature,
    private val share: TicketShareRegistry,
    context: Context
) : BookingFacade {

    private val cacheDir = context.cacheDir

    override suspend fun getBookings() = feature.getBookings()
        .map { it.map(::BookingViewFromFeature) }

    override suspend fun refresh() {
        online.getBookings()
    }

    override suspend fun saveAsFile(view: BookingView): File {
        if (view !is BookingViewActiveFromFeature) throw IllegalArgumentException()
        val shareableText = share.get(view.booking)
        val bitmap = MultiFormatWriter()
            .prepare(500, 300, BarcodeFormat.PDF_417, Color.BLACK)
            .getBitmap(shareableText.decodeToString())
        requireNotNull(bitmap)
        val file = File(cacheDir, "tickets/ticket.png")
        file.parentFile?.mkdirs()
        withContext(Dispatchers.IO) {
            file.outputStream().use { output ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
            }
        }
        bitmap.recycle()
        return file
    }

    @Suppress("FunctionName")
    private fun BookingViewFromFeature(booking: Booking) = when (booking) {
        is Booking.Active -> BookingViewActiveFromFeature(booking)
        is Booking.Expired -> BookingViewExpiredFromFeature(booking)
    }

}

fun MultiFormatWriter.prepare(
    width: Int,
    height: Int,
    format: BarcodeFormat,
    color: Int
) = FormatConfig(
    writer = this,
    width = width,
    height = height,
    format = format,
    color = color
)

class FormatConfig(
    private val writer: MultiFormatWriter,
    private val width: Int,
    private val height: Int,
    private val format: BarcodeFormat,
    private val color: Int
) {

    suspend fun getBitmap(value: String) = withContext(Dispatchers.Default) {
        if (width * height <= 0) return@withContext null
        val matrix = writer.encode(value, format, width, height)
        val width = matrix.width
        val height = matrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        for (x in 0 until matrix.width) for (y in 0 until matrix.height) {
            val pixel = if (matrix.get(x, y)) color else 0xFFFFFFFF.toInt()
            bitmap.setPixel(x, y, pixel)
        }
        bitmap
    }

}
