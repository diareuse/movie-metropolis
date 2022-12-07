package movie.core.nwk

import io.ktor.util.encodeBase64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

private class FileCache<Key : Any>(
    private val directory: File
) : Cache<Key, String> {

    private val Key.file: File
        get() {
            val name = toString().encodeBase64()
            return File(directory, name)
        }

    override suspend fun get(key: Key): String? {
        val file = key.file
        if (!file.exists() || !file.isFile)
            return null
        return withContext(Dispatchers.IO) {
            file.readText()
        }
    }

    override suspend fun put(key: Key, value: String?) {
        val file = key.file
        if (file.isDirectory) file.deleteRecursively()
        if (!file.exists()) file.createParentDirs().createNewFile()
        if (value == null) {
            file.delete()
            return
        }
        return withContext(Dispatchers.IO) {
            file.writeText(value)
        }
    }

    override suspend fun clear() {
        directory.deleteRecursively()
    }

    // ---

    private fun File.createParentDirs() = apply {
        parentFile?.mkdirs()
    }

}

fun <Key : Any> cacheOf(dir: File): Cache<Key, String> {
    return FileCache(dir)
}