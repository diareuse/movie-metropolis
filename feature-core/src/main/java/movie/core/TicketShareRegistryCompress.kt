package movie.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import movie.core.model.Booking
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

class TicketShareRegistryCompress(
    private val origin: TicketShareRegistry
) : TicketShareRegistry {

    override suspend fun add(ticket: ByteArray) {
        val unzipped = withContext(Dispatchers.IO) {
            GZIPInputStream(ticket.inputStream()).use {
                it.readBytes()
            }
        }
        origin.add(unzipped)
    }

    override suspend fun get(booking: Booking.Active): ByteArray {
        val output = ByteArrayOutputStream()
        withContext(Dispatchers.IO) {
            GZIPOutputStream(output, true).use {
                it.write(origin.get(booking))
            }
        }
        return output.use { it.toByteArray() }
    }

}