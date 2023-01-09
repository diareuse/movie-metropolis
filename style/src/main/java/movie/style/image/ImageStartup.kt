package movie.style.image

import android.content.Context
import androidx.startup.Initializer
import coil.Coil

class ImageStartup : Initializer<Coil> {

    override fun create(context: Context): Coil {
        Coil.setImageLoader(DefaultImageLoaderFactory(context))
        return Coil
    }

    override fun dependencies() = emptyList<Class<out Initializer<*>>>()

}