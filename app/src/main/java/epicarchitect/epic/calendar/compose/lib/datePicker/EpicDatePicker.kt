package epicarchitect.epic.calendar.compose.lib.datePicker

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
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
import epicarchitect.epic.calendar.compose.lib.EpicMonth
import epicarchitect.epic.calendar.compose.lib.atEndDay
import epicarchitect.epic.calendar.compose.lib.atStartDay
import epicarchitect.epic.calendar.compose.lib.basis.BasisDayOfMonthComposable
import epicarchitect.epic.calendar.compose.lib.basis.BasisDayOfWeekComposable
import epicarchitect.epic.calendar.compose.lib.basis.BasisEpicCalendar
import epicarchitect.epic.calendar.compose.lib.contains
import epicarchitect.epic.calendar.compose.lib.epicDayOfWeek
import epicarchitect.epic.calendar.compose.lib.epicMonth
import epicarchitect.epic.calendar.compose.lib.index
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

        fun mustDrawAsSingles() =
            state.selectionMode !is EpicDatePicker.SelectionMode.Range
                    || state.selectedDates.size == 1

        fun pageModifier(page: Int) = Modifier.composed {
            val basisState = BasisEpicCalendar.LocalState.current!!
            val gridInfo = basisState.dateGridInfo

            val selectionInfoList by remember(page) {
                derivedStateOf {
                    if (mustDrawAsRange()) {
                        val startDate = state.selectedDates.min()
                        val endDate = state.selectedDates.max()

                        listOfNotNull(
                            calculateSelectionInfo(
                                state = state,
                                gridInfo = gridInfo,
                                startDate = startDate,
                                endDate = endDate,
                            )
                        )
                    } else if (mustDrawAsSingles()) {
                        state.selectedDates.mapNotNull {
                            calculateSelectionInfo(
                                state = state,
                                gridInfo = gridInfo,
                                startDate = it,
                                endDate = it,
                            )
                        }
                    } else {
                        emptyList()
                    }
                }
            }

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
                    selectionInfoList.forEach { info ->
                        drawSelection(
                            selectionInfo = info,
                            color = selectionContainerColor,
                            itemContainerWidthPx = gridItemWidth,
                            itemWidthPx = columnWidthPx,
                            itemHeightPx = dayOfMonthViewHeightPx,
                            rowsSpacerHeightPx = rowsSpacerHeightPx
                        )
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

@Immutable
data class SelectionInfo(
    val gridCoordinates: Pair<IntOffset, IntOffset>,
    val isStartInGrid: Boolean,
    val isEndInGrid: Boolean
)

fun calculateSelectionInfo(
    state: EpicDatePicker.State,
    gridInfo: EpicCalendarGridInfo,
    startDate: LocalDate,
    endDate: LocalDate
): SelectionInfo? {
    val startDateOfGrid = if (state.displayDaysOfAdjacentMonths) gridInfo.dateMatrix.first().first()
    else gridInfo.currentMonth.atStartDay()

    val endDateOfGrid = if (state.displayDaysOfAdjacentMonths) gridInfo.dateMatrix.last().last()
    else gridInfo.currentMonth.atEndDay()

    if (startDate > endDateOfGrid || endDate < startDateOfGrid) return null

    val startGridOffset = if (state.displayDaysOfAdjacentMonths) 0
    else startDateOfGrid.epicDayOfWeek.index()

    val isStartInGrid = startDate >= startDateOfGrid
    val isEndInGrid = endDate <= endDateOfGrid

    val startGridItemOffset = if (isStartInGrid) {
        if (state.displayDaysOfAdjacentMonths) {
            when (startDate.epicMonth) {
                gridInfo.currentMonth -> {
                    gridInfo.currentMonth.atStartDay().epicDayOfWeek.index() + startDate.dayOfMonth - 1
                }

                gridInfo.previousMonth -> {
                    startDate.epicDayOfWeek.index()
                }

                gridInfo.nextMonth -> {
                    gridInfo.currentMonth.atStartDay().epicDayOfWeek.index() +
                            gridInfo.currentMonth.numberOfDays +
                            startDate.dayOfMonth - 1
                }

                else -> {
                    startGridOffset
                }
            }
        } else {
            startGridOffset + startDate.dayOfMonth - 1
        }
    } else {
        startGridOffset
    }

    val endGridItemOffset = if (isEndInGrid) {
        if (state.displayDaysOfAdjacentMonths) {
            when (endDate.epicMonth) {
                gridInfo.currentMonth -> {
                    gridInfo.currentMonth.atStartDay().epicDayOfWeek.index() + endDate.dayOfMonth - 1
                }

                gridInfo.previousMonth -> {
                    endDate.epicDayOfWeek.index()
                }

                gridInfo.nextMonth -> {
                    gridInfo.currentMonth.atStartDay().epicDayOfWeek.index() +
                            gridInfo.currentMonth.numberOfDays +
                            endDate.dayOfMonth - 1
                }

                else -> {
                    42
                }
            }
        } else {
            startGridOffset + endDate.dayOfMonth - 1
        }
    } else {
        if (state.displayDaysOfAdjacentMonths) 42
        else startGridOffset + gridInfo.currentMonth.numberOfDays - 1
    }

    val startCoordinates = IntOffset(
        x = startGridItemOffset % 7,
        y = startGridItemOffset / 7
    )
    val endCoordinates = IntOffset(
        x = endGridItemOffset % 7,
        y = endGridItemOffset / 7
    )
    return SelectionInfo(
        gridCoordinates = startCoordinates to endCoordinates,
        isStartInGrid = isStartInGrid,
        isEndInGrid = isEndInGrid
    )
}

fun DrawScope.drawSelection(
    selectionInfo: SelectionInfo,
    color: Color,
    itemWidthPx: Float,
    itemHeightPx: Float,
    itemContainerWidthPx: Float,
    rowsSpacerHeightPx: Float
) {
    val (x1, y1) = selectionInfo.gridCoordinates.first
    val (x2, y2) = selectionInfo.gridCoordinates.second

    val horizontalSpaceBetweenItems = itemContainerWidthPx - itemWidthPx

    val additionalStartOffsetX = if (selectionInfo.isStartInGrid) {
        (itemWidthPx + horizontalSpaceBetweenItems) / 2f
    } else {
        0f
    }

    val additionalEndOffsetX = if (selectionInfo.isEndInGrid) {
        (itemWidthPx + horizontalSpaceBetweenItems) / 2
    } else {
        itemWidthPx + horizontalSpaceBetweenItems
    }


    val startX = x1 * (itemWidthPx + horizontalSpaceBetweenItems) + additionalStartOffsetX
    val endX = x2 * (itemWidthPx + horizontalSpaceBetweenItems) + additionalEndOffsetX

    val startY = y1 * (itemHeightPx + rowsSpacerHeightPx)
    val endY = y2 * (itemHeightPx + rowsSpacerHeightPx)

    // Draw the first row background
    drawRect(
        color = color,
        topLeft = Offset(startX, startY),
        size = Size(
            width = if (y1 == y2) {
                endX - startX
            } else {
                size.width - startX
            },
            height = itemHeightPx
        )
    )

    drawRoundRect(
        color = color,
        topLeft = Offset(
            x = startX - (itemWidthPx / 2),
            y = startY
        ),
        size = Size(
            width = itemWidthPx,
            height = itemHeightPx
        ),
        cornerRadius = CornerRadius(1000f)
    )

    drawRoundRect(
        color = color,
        topLeft = Offset(
            x = endX - (itemWidthPx / 2),
            y = endY
        ),
        size = Size(
            width = itemWidthPx,
            height = itemHeightPx
        ),
        cornerRadius = CornerRadius(1000f)
    )

    if (y1 != y2) {
        for (y in y2 - y1 - 1 downTo 1) {
            drawRect(
                color = color,
                topLeft = Offset(
                    x = 0f,
                    y = startY + y * (itemHeightPx + rowsSpacerHeightPx)
                ),
                size = Size(
                    width = size.width,
                    height = itemHeightPx
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
                height = itemHeightPx
            )
        )
    }
}