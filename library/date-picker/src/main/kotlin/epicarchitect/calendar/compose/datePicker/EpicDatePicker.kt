package epicarchitect.calendar.compose.datePicker

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import epicarchitect.calendar.compose.basis.BasisDayOfMonthComposable
import epicarchitect.calendar.compose.basis.BasisDayOfWeekComposable
import epicarchitect.calendar.compose.basis.BasisEpicCalendar
import epicarchitect.calendar.compose.basis.contains
import epicarchitect.calendar.compose.pager.EpicCalendarPager
import epicarchitect.calendar.compose.ranges.drawEpicRanges
import kotlinx.datetime.LocalDate

@Composable
fun EpicDatePicker(
    modifier: Modifier = Modifier,
    state: EpicDatePicker.State = EpicDatePicker.LocalState.current
        ?: EpicDatePicker.rememberState(),
    config: EpicDatePicker.Config = EpicDatePicker.LocalConfig.current,
    dayOfWeekComposable: BasisDayOfWeekComposable = EpicDatePicker.DefaultDayOfWeekComposable,
    dayOfMonthComposable: BasisDayOfMonthComposable = EpicDatePicker.DefaultDayOfMonthComposable
) = with(config) {
    CompositionLocalProvider(
        EpicDatePicker.LocalConfig provides config,
        EpicDatePicker.LocalState provides state
    ) {
        fun mustDrawAsRange() =
            state.selectionMode is EpicDatePicker.SelectionMode.Range
                    && state.selectedDates.size > 1

        fun mustDrawAsSingles() =
            state.selectionMode !is EpicDatePicker.SelectionMode.Range
                    || state.selectedDates.size == 1

        EpicCalendarPager(
            modifier = modifier,
            pageModifier = {
                Modifier.drawEpicRanges(
                    ranges = if (mustDrawAsRange()) {
                        val startDate = state.selectedDates.min()
                        val endDate = state.selectedDates.max()
                        listOf(startDate..endDate)
                    } else if (mustDrawAsSingles()) {
                        state.selectedDates.map { it..it }
                    } else {
                        emptyList()
                    },
                    color = selectionContainerColor
                )
            },
            state = state.pagerState,
            onDayOfMonthClick = state::toggleDateSelection,
            config = pagerConfig,
            dayOfMonthComposable = dayOfMonthComposable,
            dayOfWeekComposable = dayOfWeekComposable
        )
    }
}

object EpicDatePicker {
    val DefaultDayOfMonthComposable: BasisDayOfMonthComposable = { date ->
        val basisState = BasisEpicCalendar.LocalState.current!!
        val pickerState = LocalState.current!!
        val pickerConfig = LocalConfig.current
        val selectedDays = pickerState.selectedDates

        val isSelected = when (pickerState.selectionMode) {
            is SelectionMode.Range -> when (selectedDays.size) {
                1 -> date == selectedDays.first()
                2 -> date in selectedDays.min()..selectedDays.max()
                else -> false
            }

            else -> date in selectedDays
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

    val DefaultDayOfWeekComposable: BasisDayOfWeekComposable = { dayOfWeek ->
        Text(
            text = dayOfWeek.localized(),
            textAlign = TextAlign.Center
        )
    }

    @Composable
    fun rememberState(
        selectedDates: List<LocalDate> = emptyList(),
        selectionMode: SelectionMode = SelectionMode.Single,
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
        override val selectedDates = selectedDates.toMutableStateList()
        private var _selectionMode by mutableStateOf(selectionMode)
        override var selectionMode
            get() = _selectionMode
            set(value) {
                val takeLastAmount = when (value) {
                    is SelectionMode.Multi -> value.maxSize
                    SelectionMode.Range -> 2
                    SelectionMode.Single -> 1
                }
                val last = selectedDates.takeLast(takeLastAmount)
                if (last.isNotEmpty()) {
                    selectedDates.clear()
                    selectedDates.addAll(last)
                }
                _selectionMode = value
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
            val isRemoved = selectedDates.remove(date)
            if (isRemoved) return

            when (val mode = selectionMode) {
                is SelectionMode.Single -> {
                    selectedDates.clear()
                    selectedDates.add(date)
                }

                is SelectionMode.Range -> {
                    if (selectedDates.size == 2) {
                        selectedDates.clear()
                    }
                    selectedDates.add(date)
                }

                is SelectionMode.Multi -> {
                    if (selectedDates.size == mode.maxSize) {
                        selectedDates.removeFirst()
                    }
                    selectedDates.add(date)
                }
            }
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
        val selectedDates: List<LocalDate>
        var selectionMode: SelectionMode
        val pagerState: EpicCalendarPager.State
        var displayDaysOfAdjacentMonths: Boolean
        var displayDaysOfWeek: Boolean
        fun toggleDateSelection(date: LocalDate)
    }

    sealed interface SelectionMode {
        object Single : SelectionMode
        object Range : SelectionMode
        data class Multi(val maxSize: Int = Int.MAX_VALUE) : SelectionMode
    }

    val LocalState = compositionLocalOf<State?> {
        null
    }

    val LocalConfig = compositionLocalOf<Config> {
        DefaultConfig
    }
}