package epicarchitect.calendar.compose.datePicker

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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import epicarchitect.calendar.compose.basis.BasisDayOfMonthComposable
import epicarchitect.calendar.compose.basis.BasisDayOfWeekComposable
import epicarchitect.calendar.compose.basis.BasisEpicCalendar
import epicarchitect.calendar.compose.basis.EpicCalendarConstants
import epicarchitect.calendar.compose.basis.EpicCalendarGridInfo
import epicarchitect.calendar.compose.basis.atEndDay
import epicarchitect.calendar.compose.basis.atStartDay
import epicarchitect.calendar.compose.basis.contains
import epicarchitect.calendar.compose.basis.epicDayOfWeek
import epicarchitect.calendar.compose.basis.epicMonth
import epicarchitect.calendar.compose.basis.index
import epicarchitect.calendar.compose.pager.EpicCalendarPager
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
        val contentPaddingLeft = contentPadding.calculateLeftPadding(layoutDirection)
        val contentPaddingRight = contentPadding.calculateRightPadding(layoutDirection)
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
                val itemSize = Size(columnWidthPx, dayOfMonthViewHeightPx)

                inset(
                    top = contentPaddingTop.toPx().let {
                        if (state.displayDaysOfWeek.not()) it
                        else it + dayOfWeekHeightPx + rowsSpacerHeightPx
                    },
                    bottom = contentPaddingBottom.toPx(),
                    left = contentPaddingLeft.toPx(),
                    right = contentPaddingRight.toPx()
                ) {
                    selectionInfoList.forEach { info ->
                        drawSelection(
                            selectionInfo = info,
                            color = selectionContainerColor,
                            itemContainerWidthPx = size.width / 7f,
                            itemSize = itemSize,
                            rowsSpacerHeightPx = rowsSpacerHeightPx,
                            dayOfMonthShape = pagerConfig.basisConfig.dayOfMonthViewShape
                        )
                    }
                }
            }
        }

        EpicCalendarPager(
            modifier = modifier,
            pageModifier = ::pageModifier,
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
                    EpicCalendarConstants.GridCellAmount - 1
                }
            }
        } else {
            startGridOffset + endDate.dayOfMonth - 1
        }
    } else {
        if (state.displayDaysOfAdjacentMonths) EpicCalendarConstants.GridCellAmount - 1
        else startGridOffset + gridInfo.currentMonth.numberOfDays - 1
    }

    val startCoordinates = IntOffset(
        x = startGridItemOffset % EpicCalendarConstants.DayOfWeekAmount,
        y = startGridItemOffset / EpicCalendarConstants.DayOfWeekAmount
    )
    val endCoordinates = IntOffset(
        x = endGridItemOffset % EpicCalendarConstants.DayOfWeekAmount,
        y = endGridItemOffset / EpicCalendarConstants.DayOfWeekAmount
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
    itemSize: Size,
    itemContainerWidthPx: Float,
    rowsSpacerHeightPx: Float,
    dayOfMonthShape: Shape
) {
    val (x1, y1) = selectionInfo.gridCoordinates.first
    val (x2, y2) = selectionInfo.gridCoordinates.second

    val horizontalSpaceBetweenItems = itemContainerWidthPx - itemSize.width

    val additionalStartOffsetX = if (selectionInfo.isStartInGrid) {
        (itemSize.width + horizontalSpaceBetweenItems) / 2f
    } else {
        0f
    }

    val additionalEndOffsetX = if (selectionInfo.isEndInGrid) {
        (itemSize.width + horizontalSpaceBetweenItems) / 2f
    } else {
        itemSize.width + horizontalSpaceBetweenItems
    }


    val startX = x1 * (itemSize.width + horizontalSpaceBetweenItems) + additionalStartOffsetX
    val endX = x2 * (itemSize.width + horizontalSpaceBetweenItems) + additionalEndOffsetX

    val startY = y1 * (itemSize.height + rowsSpacerHeightPx)
    val endY = y2 * (itemSize.height + rowsSpacerHeightPx)

    val itemShapeOutline = dayOfMonthShape.createOutline(
        itemSize,
        layoutDirection,
        Density(density)
    )

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
            height = itemSize.height
        )
    )

    if (selectionInfo.isStartInGrid) {
        translate(
            left = startX - (itemSize.width / 2),
            top = startY
        ) {
            drawOutline(
                outline = itemShapeOutline,
                color = color
            )
        }
    }

    if (selectionInfo.isEndInGrid) {
        translate(
            left = endX - (itemSize.width / 2),
            top = endY
        ) {
            drawOutline(
                outline = itemShapeOutline,
                color = color
            )
        }
    }

    if (y1 != y2) {
        for (y in y2 - y1 - 1 downTo 1) {
            drawRect(
                color = color,
                topLeft = Offset(
                    x = 0f,
                    y = startY + y * (itemSize.height + rowsSpacerHeightPx)
                ),
                size = Size(
                    width = size.width,
                    height = itemSize.height
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
                height = itemSize.height
            )
        )
    }
}