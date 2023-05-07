package epicarchitect.calendar.compose.datepicker.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import epicarchitect.calendar.compose.pager.state.EpicCalendarPagerState
import epicarchitect.calendar.compose.pager.state.rememberEpicCalendarPagerState
import kotlinx.datetime.LocalDate

class DefaultEpicDatePickerState(
    selectedDates: List<LocalDate>,
    selectionMode: EpicDatePickerState.SelectionMode,
    override val pagerState: EpicCalendarPagerState
) : EpicDatePickerState {
    override var selectedDates by mutableStateOf(selectedDates)
    private var _selectionMode by mutableStateOf(selectionMode)
    override var selectionMode
        get() = _selectionMode
        set(mode) {
            _selectionMode = mode
            val dates = selectedDates
            selectedDates = if (dates.isEmpty()) {
                emptyList()
            } else {
                when (mode) {
                    is EpicDatePickerState.SelectionMode.Range -> {
                        if (dates.size == 1) listOf(dates.first())
                        else listOf(dates.min(), dates.max())
                    }

                    is EpicDatePickerState.SelectionMode.Single -> {
                        dates.takeLast(mode.maxSize)
                    }
                }
            }
        }

    override var displayDaysOfAdjacentMonths
        set(value) {
            pagerState.displayDaysOfAdjacentMonths = value
        }
        get() = pagerState.displayDaysOfAdjacentMonths

    override var displayDaysOfWeek
        set(value) {
            pagerState.displayDaysOfWeek = value
        }
        get() = pagerState.displayDaysOfWeek

    override fun toggleDateSelection(date: LocalDate) {
        val dates = selectedDates.toMutableList()
        val isRemoved = dates.remove(date)
        if (isRemoved) {
            selectedDates = dates
            return
        }

        when (val mode = selectionMode) {
            is EpicDatePickerState.SelectionMode.Single -> {
                if (dates.size == mode.maxSize) {
                    dates.removeFirst()
                }
                dates.add(date)
            }

            is EpicDatePickerState.SelectionMode.Range -> {
                if (dates.size == 2) {
                    dates.clear()
                }
                dates.add(date)
            }
        }

        selectedDates = dates
    }
}

@Composable
fun rememberEpicDatePickerState(
    selectedDates: List<LocalDate> = emptyList(),
    selectionMode: EpicDatePickerState.SelectionMode = EpicDatePickerState.SelectionMode.Single(),
    pagerState: EpicCalendarPagerState = rememberEpicCalendarPagerState()
): EpicDatePickerState = remember(
    selectedDates,
    selectionMode,
    pagerState
) {
    DefaultEpicDatePickerState(
        selectedDates = selectedDates,
        selectionMode = selectionMode,
        pagerState = pagerState
    )
}