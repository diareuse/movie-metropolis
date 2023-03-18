package movie.core.di

import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope

@InstallIn(SingletonComponent::class)
@Module
class KotlinModule {

    @Provides
    fun scope(): CoroutineScope = ProcessLifecycleOwner.get().lifecycleScope

}