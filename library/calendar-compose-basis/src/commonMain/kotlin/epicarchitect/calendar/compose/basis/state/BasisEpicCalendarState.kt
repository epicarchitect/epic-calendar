package epicarchitect.calendar.compose.basis.state

import androidx.compose.runtime.compositionLocalOf
import epicarchitect.calendar.compose.basis.EpicCalendarGridInfo
import epicarchitect.calendar.compose.basis.EpicMonth

interface BasisEpicCalendarState {
    val currentMonth: EpicMonth
    val displayDaysOfAdjacentMonths: Boolean
    val displayDaysOfWeek: Boolean
    val dateGridInfo: EpicCalendarGridInfo
}

val LocalBasisEpicCalendarState = compositionLocalOf<BasisEpicCalendarState?> { null }