package epicarchitect.calendar.compose.datepicker.state

import androidx.compose.runtime.compositionLocalOf
import epicarchitect.calendar.compose.pager.state.EpicCalendarPagerState
import kotlinx.datetime.LocalDate

interface EpicDatePickerState {
    val pagerState: EpicCalendarPagerState
    val selectedDates: List<LocalDate>
    var selectionMode: SelectionMode
    var displayDaysOfAdjacentMonths: Boolean
    var displayDaysOfWeek: Boolean
    fun toggleDateSelection(date: LocalDate)

    sealed interface SelectionMode {
        data class Single(val maxSize: Int = 1) : SelectionMode
        object Range : SelectionMode
    }
}

val LocalEpicDatePickerState = compositionLocalOf<EpicDatePickerState?> {
    null
}
