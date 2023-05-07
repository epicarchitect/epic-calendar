package epicarchitect.calendar.compose.datepicker

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
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
import epicarchitect.calendar.compose.basis.BasisEpicCalendar
import epicarchitect.calendar.compose.basis.LocalBasisEpicCalendarState
import epicarchitect.calendar.compose.basis.contains
import epicarchitect.calendar.compose.basis.localized
import epicarchitect.calendar.compose.pager.EpicCalendarPager
import epicarchitect.calendar.compose.ranges.drawEpicRanges
import kotlinx.datetime.LocalDate

@Composable
fun EpicDatePicker(
    modifier: Modifier = Modifier,
    state: EpicDatePicker.State = EpicDatePicker.LocalState.current
        ?: EpicDatePicker.rememberState(),
    config: EpicDatePicker.Config = EpicDatePicker.LocalConfig.current,
    dayOfWeekContent: BasisDayOfWeekContent = EpicDatePicker.DefaultDayOfWeekContent,
    dayOfMonthContent: BasisDayOfMonthContent = EpicDatePicker.DefaultDayOfMonthContent
) = with(config) {
    CompositionLocalProvider(
        EpicDatePicker.LocalConfig provides config,
        EpicDatePicker.LocalState provides state
    ) {
        val mode = state.selectionMode
        val selectedDays = state.selectedDates
        val ranges = remember(mode, selectedDays) {
            when (mode) {
                is EpicDatePicker.SelectionMode.Range -> {
                    if (selectedDays.isEmpty()) emptyList()
                    else listOf(selectedDays.min()..selectedDays.max())
                }

                is EpicDatePicker.SelectionMode.Single -> {
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

object EpicDatePicker {
    val DefaultDayOfMonthContent: BasisDayOfMonthContent = { date ->
        val basisState = LocalBasisEpicCalendarState.current!!
        val pickerState = LocalState.current!!
        val pickerConfig = LocalConfig.current
        val selectedDays = pickerState.selectedDates
        val selectionMode = pickerState.selectionMode

        val isSelected = remember(selectionMode, selectedDays, date) {
            when (selectionMode) {
                is SelectionMode.Range -> {
                    if (selectedDays.isEmpty()) false
                    else date in selectedDays.min()..selectedDays.max()
                }

                is SelectionMode.Single -> date in selectedDays
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

    val DefaultDayOfWeekContent: BasisDayOfWeekContent = { dayOfWeek ->
        Text(
            text = dayOfWeek.localized(),
            textAlign = TextAlign.Center
        )
    }

    @Composable
    fun rememberState(
        selectedDates: List<LocalDate> = emptyList(),
        selectionMode: SelectionMode = SelectionMode.Single(),
        pagerState: EpicCalendarPager.State = EpicCalendarPager.rememberState()
    ): State = remember(
        selectedDates,
        selectionMode,
        pagerState
    ) {
        DefaultState(
            selectedDates = selectedDates,
            selectionMode = selectionMode,
            pagerState = pagerState
        )
    }

    class DefaultState(
        selectedDates: List<LocalDate>,
        selectionMode: SelectionMode,
        override val pagerState: EpicCalendarPager.State
    ) : State {
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
                        is SelectionMode.Range -> {
                            if (dates.size == 1) listOf(dates.first())
                            else listOf(dates.min(), dates.max())
                        }

                        is SelectionMode.Single -> {
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
                is SelectionMode.Single -> {
                    if (dates.size == mode.maxSize) {
                        dates.removeFirst()
                    }
                    dates.add(date)
                }

                is SelectionMode.Range -> {
                    if (dates.size == 2) {
                        dates.clear()
                    }
                    dates.add(date)
                }
            }

            selectedDates = dates
        }
    }

    val DefaultConfig = ImmutableConfig(
        pagerConfig = EpicCalendarPager.DefaultConfig,
        selectionContainerColor = Color.Red,
        selectionContentColor = Color.White
    )

    @Immutable
    data class ImmutableConfig(
        override val pagerConfig: EpicCalendarPager.Config,
        override val selectionContentColor: Color,
        override val selectionContainerColor: Color
    ) : Config

    interface Config {
        val pagerConfig: EpicCalendarPager.Config
        val selectionContentColor: Color
        val selectionContainerColor: Color
    }

    interface State {
        val pagerState: EpicCalendarPager.State
        val selectedDates: List<LocalDate>
        var selectionMode: SelectionMode
        var displayDaysOfAdjacentMonths: Boolean
        var displayDaysOfWeek: Boolean
        fun toggleDateSelection(date: LocalDate)
    }

    sealed interface SelectionMode {
        data class Single(val maxSize: Int = 1) : SelectionMode
        object Range : SelectionMode
    }

    val LocalState = compositionLocalOf<State?> {
        null
    }

    val LocalConfig = compositionLocalOf<Config> {
        DefaultConfig
    }
}