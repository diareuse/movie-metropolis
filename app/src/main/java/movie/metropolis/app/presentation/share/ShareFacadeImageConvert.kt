package movie.metropolis.app.presentation.share

import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.GlobalHistogramBinarizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ShareFacadeImageConvert(
    private val origin: ShareFacade
) : ShareFacade {

    override suspend fun putTicket(ticket: TicketRepresentation): Result<Unit> {
        if (ticket !is TicketRepresentation.Image)
            return origin.putTicket(ticket)
        val result = withContext(Dispatchers.Default) {
            val pixels = ticket.image
            val source = RGBLuminanceSource(ticket.width, ticket.height, pixels.array())
            val binaryBitmap = BinaryBitmap(GlobalHistogramBinarizer(source))
            MultiFormatReader().decode(binaryBitmap).text.let(::requireNotNull)
        }
        return origin.putTicket(TicketRepresentation.Text(result))
    }

}