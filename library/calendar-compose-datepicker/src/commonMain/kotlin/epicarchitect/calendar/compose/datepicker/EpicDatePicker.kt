package epicarchitect.calendar.compose.datepicker

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import epicarchitect.calendar.compose.basis.BasisDayOfMonthContent
import epicarchitect.calendar.compose.basis.BasisDayOfWeekContent
import epicarchitect.calendar.compose.basis.DefaultDayOfWeekContent
import epicarchitect.calendar.compose.basis.contains
import epicarchitect.calendar.compose.basis.localized
import epicarchitect.calendar.compose.basis.state.LocalBasisEpicCalendarState
import epicarchitect.calendar.compose.datepicker.config.EpicDatePickerConfig
import epicarchitect.calendar.compose.datepicker.config.LocalEpicDatePickerConfig
import epicarchitect.calendar.compose.datepicker.state.EpicDatePickerState
import epicarchitect.calendar.compose.datepicker.state.LocalEpicDatePickerState
import epicarchitect.calendar.compose.datepicker.state.rememberEpicDatePickerState
import epicarchitect.calendar.compose.pager.EpicCalendarPager
import epicarchitect.calendar.compose.pager.state.EpicCalendarPagerState
import epicarchitect.calendar.compose.pager.state.rememberEpicCalendarPagerState
import epicarchitect.calendar.compose.ranges.drawEpicRanges
import kotlinx.datetime.LocalDate

val DefaultDayOfMonthContent: BasisDayOfMonthContent = { date ->
    val basisState = LocalBasisEpicCalendarState.current!!
    val pickerState = LocalEpicDatePickerState.current!!
    val pickerConfig = LocalEpicDatePickerConfig.current
    val selectedDays = pickerState.selectedDates
    val selectionMode = pickerState.selectionMode

    val isSelected = remember(selectionMode, selectedDays, date) {
        when (selectionMode) {
            is EpicDatePickerState.SelectionMode.Range -> {
                if (selectedDays.isEmpty()) false
                else date in selectedDays.min()..selectedDays.max()
            }

            is EpicDatePickerState.SelectionMode.Single -> date in selectedDays
        }
    }

    Text(
        modifier = Modifier.alpha(
            if (date in basisState.currentMonth) 1.0f
            else 0.5f
        ),
        text = date.dayOfMonth.toString(),
        textAlign = TextAlign.Center,
        color = if (isSelected) pickerConfig.selectionContentColor
        else Color.Unspecified
    )
}

@Composable
fun EpicDatePicker(
    modifier: Modifier = Modifier,
    state: EpicDatePickerState = LocalEpicDatePickerState.current
        ?: rememberEpicDatePickerState(),
    config: EpicDatePickerConfig = LocalEpicDatePickerConfig.current,
    dayOfWeekContent: BasisDayOfWeekContent = DefaultDayOfWeekContent,
    dayOfMonthContent: BasisDayOfMonthContent = DefaultDayOfMonthContent
) = with(config) {
    CompositionLocalProvider(
        LocalEpicDatePickerConfig provides config,
        LocalEpicDatePickerState provides state
    ) {
        val mode = state.selectionMode
        val selectedDays = state.selectedDates
        val ranges = remember(mode, selectedDays) {
            when (mode) {
                is EpicDatePickerState.SelectionMode.Range -> {
                    if (selectedDays.isEmpty()) emptyList()
                    else listOf(selectedDays.min()..selectedDays.max())
                }

                is EpicDatePickerState.SelectionMode.Single -> {
                    selectedDays.map { it..it }
                }
            }
        }

        EpicCalendarPager(
            modifier = modifier,
            pageModifier = {
                Modifier.drawEpicRanges(
                    ranges = ranges,
                    color = selectionContainerColor
                )
            },
            state = state.pagerState,
            onDayOfMonthClick = state::toggleDateSelection,
            config = pagerConfig,
            dayOfMonthContent = dayOfMonthContent,
            dayOfWeekContent = dayOfWeekContent
        )
    }
}
