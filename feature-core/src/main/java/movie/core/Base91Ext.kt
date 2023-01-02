package movie.core

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream

fun Base91.encode(bytes: ByteArray): ByteArray = ByteArrayOutputStream().use { output ->
    bytes.inputStream().use { input ->
        encode(input, output)
    }
    output.toByteArray()
}

fun Base91.encode(input: InputStream, output: OutputStream) {
    var s: Int
    val ibuf = ByteArray(53248)
    val obuf = ByteArray(65536)

    while (input.read(ibuf).also { s = it } > 0) {
        s = encode(ibuf, s, obuf)
        output.write(obuf, 0, s)
    }
    s = encEnd(obuf)
    output.write(obuf, 0, s)
}

fun Base91.decode(bytes: ByteArray): ByteArray = ByteArrayOutputStream().use { output ->
    bytes.inputStream().use { input ->
        decode(input, output)
    }
    output.toByteArray()
}

fun Base91.decode(input: InputStream, output: OutputStream) {
    var s: Int
    val ibuf = ByteArray(65536)
    val obuf = ByteArray(57344)

    while (input.read(ibuf).also { s = it } > 0) {
        s = decode(ibuf, s, obuf)
        output.write(obuf, 0, s)
    }
    s = decEnd(obuf)
    output.write(obuf, 0, s)
}

fun ByteArray.base91Decode() = Base91().decode(this)
fun ByteArray.base91Encode() = Base91().encode(this)