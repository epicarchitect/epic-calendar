package epicarchitect.epic.calendar.compose.lib.basis

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import androidx.compose.ui.platform.LocalLayoutDirection
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
    onDayOfMonthClick: ((LocalDate) -> Unit)? = null,
    onDayOfWeekClick: ((EpicDayOfWeek) -> Unit)? = null,
    config: BasisEpicCalendar.Config = BasisEpicCalendar.LocalConfig.current,
    dayOfWeekComposable: BasisDayOfWeekComposable = BasisEpicCalendar.DefaultDayOfWeekComposable,
    dayOfMonthComposable: BasisDayOfMonthComposable = BasisEpicCalendar.DefaultDayOfMonthComposable
) = with(config) {
    CompositionLocalProvider(
        BasisEpicCalendar.LocalConfig provides config,
        BasisEpicCalendar.LocalState provides state
    ) {
        val layoutDirection = LocalLayoutDirection.current
        val contentPaddingTop = contentPadding.calculateTopPadding()
        val contentPaddingStart = contentPadding.calculateStartPadding(layoutDirection)
        val contentPaddingEnd = contentPadding.calculateEndPadding(layoutDirection)
        val contentPaddingBottom = contentPadding.calculateBottomPadding()

        Column(modifier) {
            if (state.displayDaysOfWeek) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = contentPaddingTop,
                            start = contentPaddingStart,
                            end = contentPaddingEnd,
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,

                    ) {
                    state.dateGridInfo.daysOfWeek.forEach { dayOfWeek ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(dayOfWeekViewShape)
                                .height(dayOfWeekViewHeight)
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

                Spacer(modifier = Modifier.height(rowsSpacerHeight))
            }

            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth(),
                state = state.daysGridState,
                columns = GridCells.Fixed(count = 7),
                verticalArrangement = Arrangement.spacedBy(rowsSpacerHeight),
                horizontalArrangement = Arrangement.SpaceBetween,
                userScrollEnabled = false,
                contentPadding = PaddingValues(
                    top = if (state.displayDaysOfWeek) 0.dp else contentPaddingTop,
                    start = contentPaddingStart,
                    end = contentPaddingEnd,
                    bottom = contentPaddingBottom
                )
            ) {
                state.dateGridInfo.dateMatrix.forEachIndexed { rowIndex, rowDates ->
                    itemsIndexed(
                        items = rowDates,
                        key = { index, key -> "[$rowIndex][$index]" },
                        contentType = { _, _ -> "date" }
                    ) { _, date ->
                        if (state.displayDaysOfAdjacentMonths || date.epicMonth == state.currentMonth) {
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
        val gridState = rememberLazyGridState()

        return remember(
            currentMonth,
            displayDaysOfWeek,
            displayDaysOfAdjacentMonths,
            gridState,
        ) {
            DefaultState(
                currentMonth = currentMonth,
                displayDaysOfWeek = displayDaysOfWeek,
                displayDaysOfAdjacentMonths = displayDaysOfAdjacentMonths,
                daysGridState = gridState,
            )
        }
    }

    class DefaultState(
        override val currentMonth: EpicMonth,
        displayDaysOfAdjacentMonths: Boolean,
        displayDaysOfWeek: Boolean,
        override val daysGridState: LazyGridState
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
        val daysGridState: LazyGridState
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