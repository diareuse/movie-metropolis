package movie.metropolis.app.util

import android.util.Base64

fun String.decodeBase64() =
    Base64.decode(this, Base64.URL_SAFE or Base64.NO_PADDING).decodeToString()

fun String.encodeBase64() =
    Base64.encode(encodeToByteArray(), Base64.URL_SAFE or Base64.NO_PADDING).decodeToString()