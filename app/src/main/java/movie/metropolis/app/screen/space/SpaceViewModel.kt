package movie.metropolis.app.screen.space

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import movie.metropolis.app.model.DeleteProgress
import movie.metropolis.app.model.DiskSpace.Companion.bytes
import java.io.File
import javax.inject.Inject

@Stable
@HiltViewModel
class SpaceViewModel @Inject constructor(
    @ApplicationContext
    context: Context
) : ViewModel() {

    private val pkg = context.packageName
    private val filesDir = context.filesDir
    private val cacheDir = context.cacheDir
    private val databaseDir = File(context.filesDir.parentFile, "databases")

    // ---

    private val postersFile get() = File(cacheDir, "image_cache").walk()
    private val databaseCoreFile
        get() = databaseDir.walk().filter { it.name.startsWith("$pkg.core") }
    private val databaseRatingFile
        get() = databaseDir.walk().filter { it.name.startsWith("$pkg.rating") }

    // ---

    private val refresh = Channel<Unit>()
    private val refreshFlow = refresh.receiveAsFlow().onStart { emit(Unit) }

    private val posters = refreshFlow.map {
        postersFile.sumOf { it.length() }.bytes
    }

    private val databaseCore = refreshFlow.map {
        databaseCoreFile.sumOf { it.length() }.bytes
    }

    private val databaseRating = refreshFlow.map {
        databaseRatingFile.sumOf { it.length() }.bytes
    }

    val state = combine(
        posters,
        databaseCore,
        databaseRating
    ) { posters, databaseCore, databaseRating ->
        SpaceState(posters, databaseCore, databaseRating)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SpaceState())

    // ---

    fun deletePosters() = deleteFlow(postersFile).onCompletion { refresh.trySend(Unit) }
    fun deleteCore() = deleteFlow(databaseCoreFile).onCompletion { refresh.trySend(Unit) }
    fun deleteRating() = deleteFlow(databaseRatingFile).onCompletion { refresh.trySend(Unit) }

    private fun deleteFlow(files: Sequence<File>) = flow {
        emit(DeleteProgress(0, 0))
        val files = files.toList()
        var progress = DeleteProgress(0, files.size)
        emit(progress++)
        for (file in files) {
            withContext(Dispatchers.IO) {
                file.delete()
            }
            emit(progress++)
        }
    }

}

