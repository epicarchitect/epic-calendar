package epicarchitect.epic.calendar.compose.lib.datePicker

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.lazy.grid.LazyGridItemInfo
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
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import epicarchitect.epic.calendar.compose.lib.EpicCalendarGridInfo
import epicarchitect.epic.calendar.compose.lib.EpicGridPosition
import epicarchitect.epic.calendar.compose.lib.atDay
import epicarchitect.epic.calendar.compose.lib.basis.BasisDayOfMonthComposable
import epicarchitect.epic.calendar.compose.lib.basis.BasisDayOfWeekComposable
import epicarchitect.epic.calendar.compose.lib.basis.BasisEpicCalendar
import epicarchitect.epic.calendar.compose.lib.contains
import epicarchitect.epic.calendar.compose.lib.pager.EpicCalendarPager
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
        val layoutDirection = LocalLayoutDirection.current
        val contentPadding = pagerConfig.basisConfig.contentPadding
        val contentPaddingTop = contentPadding.calculateTopPadding()
        val contentPaddingStart = contentPadding.calculateStartPadding(layoutDirection)
        val contentPaddingEnd = contentPadding.calculateEndPadding(layoutDirection)
        val contentPaddingBottom = contentPadding.calculateBottomPadding()

        fun mustDrawAsRange() =
            state.selectionMode is EpicDatePicker.SelectionMode.Range
                    && state.selectedDates.size > 1

        fun mustDrawAsSingle() =
            state.selectionMode !is EpicDatePicker.SelectionMode.Range
                    || state.selectedDates.size == 1

        @Suppress("UNUSED_PARAMETER")
        fun pageModifier(page: Int) = Modifier.composed {
            val basisState = BasisEpicCalendar.LocalState.current!!
            val gridInfo = basisState.dateGridInfo
            val month = basisState.currentMonth

            drawBehind {
                val dayOfWeekHeightPx = pagerConfig.basisConfig.dayOfWeekViewHeight.toPx()
                val dayOfMonthViewHeightPx = pagerConfig.basisConfig.dayOfMonthViewHeight.toPx()
                val rowsSpacerHeightPx = pagerConfig.basisConfig.rowsSpacerHeight.toPx()
                val columnWidthPx = pagerConfig.basisConfig.columnWidth.toPx()
                val gridItemWidth = size.width / 7f

                inset(
                    top = contentPaddingTop.toPx().let {
                        if (state.displayDaysOfWeek.not()) it
                        else it + dayOfWeekHeightPx + rowsSpacerHeightPx
                    },
                    bottom = contentPaddingBottom.toPx(),
                    left = contentPaddingStart.toPx(),
                    right = contentPaddingEnd.toPx()
                ) {
                    if (mustDrawAsRange()) {
                        val startDate = state.selectedDates.min()
                        val endDate = state.selectedDates.max()

                        SelectedRangeInfo.calculateRangeInfo(
                            startDate = startDate,
                            endDate = endDate,
                            gridInfo = gridInfo,
                            displayDaysOfAdjacentMonths = state.displayDaysOfAdjacentMonths
                        ).let {
                            drawRangeBackground(
                                selectedRangeInfo = it,
                                color = selectionContainerColor,
                                itemContainerWidthPx = columnWidthPx,
                                itemContainerHeightPx = dayOfMonthViewHeightPx,
                                horizontalSpaceBetweenItemsPx = gridItemWidth - columnWidthPx,
                                rowsSpacerHeightPx = rowsSpacerHeightPx
                            )
                        }
                    } else if (mustDrawAsSingle()) {
                        state.selectedDates.forEach {
                            SelectedRangeInfo.calculateRangeInfo(
                                startDate = it,
                                endDate = it,
                                gridInfo = gridInfo,
                                displayDaysOfAdjacentMonths = state.displayDaysOfAdjacentMonths
                            ).let {
                                drawRangeBackground(
                                    selectedRangeInfo = it,
                                    color = selectionContainerColor,
                                    itemContainerWidthPx = columnWidthPx,
                                    itemContainerHeightPx = dayOfMonthViewHeightPx,
                                    horizontalSpaceBetweenItemsPx = gridItemWidth - columnWidthPx,
                                    rowsSpacerHeightPx = rowsSpacerHeightPx
                                )
                            }
                        }
                    }
                }
            }
        }

        EpicCalendarPager(
            modifier = modifier,
            pageModifier = ::pageModifier,
            state = state.pagerState,
            onDayOfMonthClick = state::toggleCellSelection,
            config = pagerConfig,
            dayOfMonthComposable = dayOfMonthComposable,
            dayOfWeekComposable = dayOfWeekComposable
        )
    }
}

private fun EpicGridPosition.toOffset(
    drawSize: Size,
    gridInfo: EpicCalendarGridInfo
) = Offset(
    x = column * drawSize.width / (gridInfo.maxPosition.column + 1),
    y = row * drawSize.height / (gridInfo.maxPosition.row + 1),
)

private fun List<LazyGridItemInfo>.filterBy(
    dates: List<LocalDate>,
    gridInfo: EpicCalendarGridInfo
) = filter {
    gridInfo.dateMatrix[it.row][it.column] in dates
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

        override fun toggleCellSelection(date: LocalDate) {
            val isRemoved = selectedDates.removeIf { it == date }
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
        fun toggleCellSelection(date: LocalDate)
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

internal class SelectedRangeInfo(
    val gridCoordinates: Pair<IntOffset?, IntOffset?>,
    val firstIsSelectionStart: Boolean,
    val lastIsSelectionEnd: Boolean
) {
    companion object {
        fun calculateRangeInfo(
            displayDaysOfAdjacentMonths: Boolean,
            gridInfo: EpicCalendarGridInfo,
            startDate: LocalDate,
            endDate: LocalDate
        ): SelectedRangeInfo {
            val monthStartDay = if (displayDaysOfAdjacentMonths) {
                gridInfo.dateMatrix.first().first()
            } else {
                gridInfo.currentMonth.atDay(1)
            }

            val monthEndDay = if (displayDaysOfAdjacentMonths) {
                gridInfo.dateMatrix.last().last()
            } else {
                gridInfo.currentMonth.atDay(gridInfo.currentMonth.numberOfDays)
            }

            val fixedStartDate = if (startDate < monthStartDay) monthStartDay else startDate
            val fixedEndDate = if (endDate > monthEndDay) monthEndDay else endDate

            val firstIsSelectionStart = startDate >= monthStartDay
            val lastIsSelectionEnd = endDate <= monthEndDay

            return SelectedRangeInfo(
                Pair(
                    gridInfo.dateInfoMap[fixedStartDate]?.let {
                        IntOffset(it.position.column, it.position.row)
                    },
                    gridInfo.dateInfoMap[fixedEndDate]?.let {
                        IntOffset(it.position.column, it.position.row)
                    }
                ),
                firstIsSelectionStart,
                lastIsSelectionEnd
            )
        }
    }
}

internal fun DrawScope.drawRangeBackground(
    selectedRangeInfo: SelectedRangeInfo,
    color: Color,
    itemContainerWidthPx: Float,
    itemContainerHeightPx: Float,
    horizontalSpaceBetweenItemsPx: Float,
    rowsSpacerHeightPx: Float
) {
    if (selectedRangeInfo.gridCoordinates.first == null || selectedRangeInfo.gridCoordinates.second == null) return

    val (x1, y1) = selectedRangeInfo.gridCoordinates.first!!
    val (x2, y2) = selectedRangeInfo.gridCoordinates.second!!

    val additionalStartX = if (selectedRangeInfo.firstIsSelectionStart) {
        (itemContainerWidthPx + horizontalSpaceBetweenItemsPx) / 2
    } else {
        0f
    }

    val additionalEndX = if (selectedRangeInfo.lastIsSelectionEnd) {
        (itemContainerWidthPx + horizontalSpaceBetweenItemsPx) / 2
    } else {
        itemContainerWidthPx + horizontalSpaceBetweenItemsPx
    }

    val startX = x1 * (itemContainerWidthPx + horizontalSpaceBetweenItemsPx) + additionalStartX
    val startY = y1 * (itemContainerHeightPx + rowsSpacerHeightPx)
    val endX = x2 * (itemContainerWidthPx + horizontalSpaceBetweenItemsPx) + additionalEndX
    val endY = y2 * (itemContainerHeightPx + rowsSpacerHeightPx)

    if (selectedRangeInfo.firstIsSelectionStart) {
        drawRoundRect(
            color = color,
            topLeft = Offset(
                x = startX - itemContainerWidthPx / 2f,
                y = startY
            ),
            size = Size(
                width = itemContainerWidthPx,
                height = itemContainerHeightPx
            ),
            cornerRadius = CornerRadius(1000f, 1000f)
        )
    }

    if (selectedRangeInfo.lastIsSelectionEnd && (x1 != x2 || y1 != y2)) {
        drawRoundRect(
            color = color,
            topLeft = Offset(
                x = endX - itemContainerWidthPx / 2,
                y = endY
            ),
            size = Size(
                width = itemContainerWidthPx,
                height = itemContainerHeightPx
            ),
            cornerRadius = CornerRadius(1000f, 1000f)
        )
    }

    drawRect(
        color = color,
        topLeft = Offset(startX, startY),
        size = Size(
            width = if (y1 == y2) endX - startX else this.size.width - startX,
            height = itemContainerHeightPx
        )
    )

    if (y1 != y2) {
        for (y in y2 - y1 - 1 downTo 1) {
            drawRect(
                color = color,
                topLeft = Offset(
                    x = 0f,
                    y = startY + y * (itemContainerHeightPx + rowsSpacerHeightPx)
                ),
                size = Size(
                    width = this.size.width,
                    height = itemContainerHeightPx
                )
            )
        }

        drawRect(
            color = color,
            topLeft = Offset(
                x = 0f,
                y = endY
            ),
            size = Size(
                width = endX,
                height = itemContainerHeightPx
            )
        )
    }
}