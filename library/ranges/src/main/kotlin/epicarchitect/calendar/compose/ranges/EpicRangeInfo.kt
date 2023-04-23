package epicarchitect.calendar.compose.ranges

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.IntOffset

@Immutable
data class EpicRangeInfo(
    val gridCoordinates: Pair<IntOffset, IntOffset>,
    val isStartInGrid: Boolean,
    val isEndInGrid: Boolean
)