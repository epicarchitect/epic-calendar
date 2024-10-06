package epicarchitect.calendar.compose.basis.state

import androidx.compose.runtime.compositionLocalOf
import epicarchitect.calendar.compose.basis.EpicMonth
import epicarchitect.calendar.compose.basis.config.BasisEpicCalendarConfig

interface BasisEpicCalendarState {
    val config: BasisEpicCalendarConfig
    val currentMonth: EpicMonth
    val dateGridInfo: EpicCalendarGridInfo
}

val LocalBasisEpicCalendarState = compositionLocalOf<BasisEpicCalendarState?> { null }