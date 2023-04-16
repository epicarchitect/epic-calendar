package epicarchitect.epic.calendar.compose.lib.basis

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import epicarchitect.epic.calendar.compose.lib.EpicCalendarGridInfo
import epicarchitect.epic.calendar.compose.lib.EpicDayOfWeek
import epicarchitect.epic.calendar.compose.lib.EpicMonth
import epicarchitect.epic.calendar.compose.lib.calculateEpicDateGridInfo
import epicarchitect.epic.calendar.compose.lib.contains
import epicarchitect.epic.calendar.compose.lib.epicMonth
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

typealias BasisDayOfMonthComposable = @Composable BoxScope.(LocalDate) -> Unit
typealias BasisDayOfWeekComposable = @Composable BoxScope.(EpicDayOfWeek) -> Unit

@Composable
fun BasisEpicCalendar(
    modifier: Modifier = Modifier,
    state: BasisEpicCalendar.State = BasisEpicCalendar.LocalState.current
        ?: BasisEpicCalendar.rememberState(),
    config: BasisEpicCalendar.Config = BasisEpicCalendar.LocalConfig.current,
    onDayOfMonthClick: ((LocalDate) -> Unit)? = null,
    onDayOfWeekClick: ((EpicDayOfWeek) -> Unit)? = null,
    dayOfWeekComposable: BasisDayOfWeekComposable = BasisEpicCalendar.DefaultDayOfWeekComposable,
    dayOfMonthComposable: BasisDayOfMonthComposable = BasisEpicCalendar.DefaultDayOfMonthComposable
) = with(config) {
    CompositionLocalProvider(
        BasisEpicCalendar.LocalConfig provides config,
        BasisEpicCalendar.LocalState provides state
    ) {
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(count = 7),
            verticalArrangement = Arrangement.spacedBy(rowsSpacerHeight),
            horizontalArrangement = Arrangement.SpaceBetween,
            userScrollEnabled = false,
            contentPadding = contentPadding
        ) {
            if (state.displayDaysOfWeek) {
                state.dateGridInfo.daysOfWeek.forEach { dayOfWeek ->
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(dayOfWeekViewShape)
                                    .height(dayOfWeekViewHeight)
                                    .width(columnWidth)
                                    .let {
                                        if (onDayOfWeekClick == null) it
                                        else it.clickable {
                                            onDayOfWeekClick(dayOfWeek)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                dayOfWeekComposable(dayOfWeek)
                            }
                        }
                    }
                }
            }

            state.dateGridInfo.dateMatrix.forEachIndexed { rowIndex, rowDates ->
                rowDates.forEachIndexed { columnIndex, date ->
                    if (state.displayDaysOfAdjacentMonths || date.epicMonth == state.currentMonth) {
                        item(
                            key = "[$rowIndex][$columnIndex]",
                            contentType = "date"
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(dayOfMonthViewShape)
                                        .height(dayOfMonthViewHeight)
                                        .width(columnWidth)
                                        .let {
                                            if (onDayOfMonthClick == null) it
                                            else it.clickable {
                                                onDayOfMonthClick(date)
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    dayOfMonthComposable(date)
                                }
                            }
                        }
                    } else {
                        item(
                            key = "[$rowIndex][$columnIndex]",
                            contentType = "empty"
                        ) {}
                    }
                }
            }
        }
    }
}

object BasisEpicCalendar {
    val DefaultDayOfMonthComposable: BasisDayOfMonthComposable = { date ->
        val state = LocalState.current!!
        Text(
            modifier = Modifier.alpha(
                if (date in state.currentMonth) 1.0f
                else 0.5f
            ),
            text = date.dayOfMonth.toString(),
            textAlign = TextAlign.Center
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
        currentMonth: EpicMonth = EpicMonth.now(TimeZone.currentSystemDefault()),
        displayDaysOfWeek: Boolean = true,
        displayDaysOfAdjacentMonths: Boolean = true
    ): State {
        return remember(
            currentMonth,
            displayDaysOfWeek,
            displayDaysOfAdjacentMonths
        ) {
            DefaultState(
                currentMonth = currentMonth,
                displayDaysOfWeek = displayDaysOfWeek,
                displayDaysOfAdjacentMonths = displayDaysOfAdjacentMonths
            )
        }
    }

    class DefaultState(
        override val currentMonth: EpicMonth,
        displayDaysOfAdjacentMonths: Boolean,
        displayDaysOfWeek: Boolean
    ) : State {
        override var displayDaysOfAdjacentMonths by mutableStateOf(displayDaysOfAdjacentMonths)
        override var displayDaysOfWeek by mutableStateOf(displayDaysOfWeek)

        private val firstDayOfWeek get() = EpicDayOfWeek.firstDayOfWeekByLocale()

        override val dateGridInfo by derivedStateOf {
            calculateEpicDateGridInfo(
                currentMonth = currentMonth,
                displayDaysOfAdjacentMonths = this.displayDaysOfAdjacentMonths,
                firstDayOfWeek = firstDayOfWeek
            )
        }
    }

    val DefaultConfig = ImmutableConfig(
        rowsSpacerHeight = 4.dp,
        dayOfWeekViewHeight = 40.dp,
        dayOfMonthViewHeight = 40.dp,
        columnWidth = 40.dp,
        dayOfWeekViewShape = CircleShape,
        dayOfMonthViewShape = CircleShape,
        contentPadding = PaddingValues(0.dp)
    )

    @Immutable
    data class ImmutableConfig(
        override val rowsSpacerHeight: Dp,
        override val dayOfWeekViewHeight: Dp,
        override val dayOfMonthViewHeight: Dp,
        override val columnWidth: Dp,
        override val dayOfWeekViewShape: Shape,
        override val dayOfMonthViewShape: Shape,
        override val contentPadding: PaddingValues
    ) : Config

    interface State {
        val currentMonth: EpicMonth
        var displayDaysOfAdjacentMonths: Boolean
        var displayDaysOfWeek: Boolean
        val dateGridInfo: EpicCalendarGridInfo
    }

    interface Config {
        val rowsSpacerHeight: Dp
        val dayOfWeekViewHeight: Dp
        val dayOfMonthViewHeight: Dp
        val columnWidth: Dp
        val dayOfWeekViewShape: Shape
        val dayOfMonthViewShape: Shape
        val contentPadding: PaddingValues
    }

    val LocalState = compositionLocalOf<State?> {
        null
    }

    val LocalConfig = compositionLocalOf<Config> {
        DefaultConfig
    }
}