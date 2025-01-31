package movie.style.util

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*

private val DpSaver = Saver<Dp, Float>({ it.value }, { it.dp })
val Dp.Companion.Saver
    get() = DpSaver

@Composable
fun rememberSaveable(
    init: Dp,
    vararg inputs: Any?,
    key: String? = null
): MutableState<Dp> {
    return rememberSaveable(
        inputs = inputs,
        stateSaver = Dp.Saver,
        key = key,
        init = { mutableStateOf(init) }
    )
}

private val TextUnitSaver = Saver<TextUnit, Float>({ it.value }, { it.sp })
val TextUnit.Companion.Saver
    get() = TextUnitSaver

@Composable
fun rememberSaveable(
    init: TextUnit,
    vararg inputs: Any?,
    key: String? = null
): MutableState<TextUnit> {
    return rememberSaveable(
        inputs = inputs,
        stateSaver = TextUnit.Saver,
        key = key,
        init = { mutableStateOf(init) }
    )
}

private val DpOffsetSaver = listSaver(
    save = { listOf(it.x.value, it.y.value) },
    restore = { it: List<Float> -> DpOffset(it[0].dp, it[1].dp) }
)
val DpOffset.Companion.Saver
    get() = DpOffsetSaver

@Composable
fun rememberSaveable(
    init: DpOffset,
    vararg inputs: Any?,
    key: String? = null
): MutableState<DpOffset> {
    return rememberSaveable(
        inputs = inputs,
        stateSaver = DpOffset.Saver,
        key = key,
        init = { mutableStateOf(init) }
    )
}

private val OffsetSaver = listSaver({ listOf(it.x, it.y) }, { Offset(it[0], it[1]) })
val Offset.Companion.Saver
    get() = OffsetSaver

@Composable
fun rememberSaveable(
    init: Offset,
    vararg inputs: Any?,
    key: String? = null
): MutableState<Offset> {
    return rememberSaveable(
        inputs = inputs,
        stateSaver = Offset.Saver,
        key = key,
        init = { mutableStateOf(init) }
    )
}

private val IntOffsetSaver = listSaver({ listOf(it.x, it.y) }, { IntOffset(it[0], it[1]) })
val IntOffset.Companion.Saver
    get() = IntOffsetSaver

@Composable
fun rememberSaveable(
    vararg inputs: Any?,
    key: String? = null,
    init: IntOffset
): MutableState<IntOffset> {
    return rememberSaveable(
        inputs = inputs,
        stateSaver = IntOffset.Saver,
        key = key,
        init = { mutableStateOf(init) }
    )
}

private val ColorSaver = Saver<Color, Int>({ it.toArgb() }, { Color(it) })
val Color.Companion.Saver
    get() = ColorSaver

@Composable
fun rememberSaveable(
    init: Color,
    vararg inputs: Any?,
    key: String? = null
): MutableState<Color> {
    return rememberSaveable(
        inputs = inputs,
        stateSaver = Color.Saver,
        key = key,
        init = { mutableStateOf(init) }
    )
}