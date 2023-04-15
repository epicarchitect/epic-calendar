package epicarchitect.epic.calendar.compose.lib.datePicker

import android.util.Log
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.lazy.grid.LazyGridItemInfo
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
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.toOffset
import androidx.compose.ui.unit.toSize
import epicarchitect.epic.calendar.compose.lib.basis.BasisDayOfMonthComposable
import epicarchitect.epic.calendar.compose.lib.basis.BasisDayOfWeekComposable
import epicarchitect.epic.calendar.compose.lib.basis.BasisEpicCalendar
import epicarchitect.epic.calendar.compose.lib.contains
import epicarchitect.epic.calendar.compose.lib.index
import epicarchitect.epic.calendar.compose.lib.pager.EpicCalendarPager
import epicarchitect.epic.calendar.compose.lib.toEpic
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
                    && state.selectedDays.size > 1

        fun mustDrawAsSingle() =
            state.selectionMode !is EpicDatePicker.SelectionMode.Range
                    || state.selectedDays.size == 1

        @Suppress("UNUSED_PARAMETER")
        fun pageModifier(page: Int) = Modifier.composed {
            val basisState = BasisEpicCalendar.LocalState.current!!
            val gridLayoutInfo by remember {
                derivedStateOf {
                    basisState.daysGridState.layoutInfo
                }
            }
            val itemsInfo = gridLayoutInfo.visibleItemsInfo

            val selectedItemsInfo = itemsInfo.filterBy(
                days = state.selectedDays,
                daysMatrix = basisState.daysMatrix
            )


            drawBehind {
                val dayOfWeekHeightPx = pagerConfig.basisConfig.dayOfWeekViewHeight.toPx()
                val dayOfMonthViewHeightPx = pagerConfig.basisConfig.dayOfMonthViewHeight.toPx()
                val rowsSpacerHeightPx = pagerConfig.basisConfig.rowsSpacerHeight.toPx()
                val columnWidthPx = pagerConfig.basisConfig.columnWidth.toPx()

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
                        val startDay = state.selectedDays.min()
                        val endDay = state.selectedDays.max()
                        itemsInfo.forEach { itemInfo ->
                            try {
                                val date = LocalDate.parse(itemInfo.key.toString())
                                if (date in startDay..endDay) {
                                    val itemWidth = itemInfo.size.width + 1f // +1f to fix some right space
                                    Log.d("test123", "itemWidth: $itemWidth")
                                    Log.d("test123", "size: ${size.width}")

                                    val weekDayIndex = date.dayOfWeek.toEpic().index()

                                    val additionalOffset = if (startDay == date) {
                                        Offset(
                                            x = itemWidth / 2f,
                                            y = 0f
                                        )
                                    } else if (endDay == date) {
                                        Offset(
                                            x = -(itemWidth / 2f),
                                            y = 0f
                                        )
                                    } else Offset.Zero

                                    drawRect(
                                        color = selectionContainerColor,
                                        topLeft = Offset(
                                            x = itemWidth * weekDayIndex,
                                            y = (dayOfMonthViewHeightPx + rowsSpacerHeightPx) * itemInfo.row
                                        ) + additionalOffset,
                                        size = Size(
                                            width = itemWidth,
                                            height = dayOfMonthViewHeightPx
                                        )
                                    )
                                }
                            } catch (e: Exception) {
                                /* no-op */
                            }
                        }

                        selectedItemsInfo.forEach { info ->
                            // kostil to achieve center

                            val additionalOffset = Offset(
                                x = (info.size.width - columnWidthPx) / 2f,
                                y = 0f
                            )

                            val topLeft = info.offset.toOffset() + additionalOffset
                            val size = Size(
                                width = columnWidthPx,
                                height = dayOfMonthViewHeightPx
                            )

                            drawRoundRect(
                                color = selectionContainerColor,
                                topLeft = topLeft,
                                size = size,
                                cornerRadius = CornerRadius(1000f)
                            )
                        }
                    } else if (mustDrawAsSingle()) {
                        selectedItemsInfo.forEach { info ->
                            // kostil to achieve center

                            val additionalOffset = Offset(
                                x = (info.size.width - columnWidthPx) / 2f,
                                y = 0f
                            )

                            val topLeft = info.offset.toOffset() + additionalOffset
                            val size = Size(
                                width = columnWidthPx,
                                height = dayOfMonthViewHeightPx
                            )

                            drawRoundRect(
                                color = selectionContainerColor,
                                topLeft = topLeft,
                                size = size,
                                cornerRadius = CornerRadius(1000f)
                            )
                        }
                    }
                }
            }
        }

        EpicCalendarPager(
            modifier = modifier,
            pageModifier = ::pageModifier,
            state = state.pagerState,
            onDayOfMonthClick = state::toggleDaySelection,
            config = pagerConfig,
            dayOfMonthComposable = dayOfMonthComposable,
            dayOfWeekComposable = dayOfWeekComposable
        )
    }
}

private fun List<LazyGridItemInfo>.filterBy(
    days: List<LocalDate>,
    daysMatrix: List<List<LocalDate?>>
) = filter {
    daysMatrix.getOrNull(it.row)?.getOrNull(it.column) in days
}

object EpicDatePicker {
    val DefaultDayOfMonthComposable: BasisDayOfMonthComposable = { date ->
        if (date != null) {
            val basisState = BasisEpicCalendar.LocalState.current!!
            val pickerState = LocalState.current!!
            val pickerConfig = LocalConfig.current
            val selectedDays = pickerState.selectedDays
            val selectionMode = pickerState.selectionMode
            val isSelected = remember(selectionMode, selectedDays, date, selectedDays.size) {
                when (selectionMode) {
                    is SelectionMode.Range -> when (selectedDays.size) {
                        1 -> date == selectedDays.first()
                        2 -> date in selectedDays.min()..selectedDays.max()
                        else -> false
                    }
                    else -> date in selectedDays
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
    }

    val DefaultDayOfWeekComposable: BasisDayOfWeekComposable = { dayOfWeek ->
        Text(
            text = dayOfWeek.localized(),
            textAlign = TextAlign.Center
        )
    }

    @Composable
    fun rememberState(
        selectedDays: List<LocalDate> = emptyList(),
        selectionMode: SelectionMode = SelectionMode.Single,
        pagerState: EpicCalendarPager.State = EpicCalendarPager.rememberState()
    ): State = remember(
        selectedDays,
        selectionMode,
        pagerState
    ) {
        DefaultState(
            selectedDays = selectedDays,
            selectionMode = selectionMode,
            pagerState = pagerState
        )
    }

    class DefaultState(
        selectedDays: List<LocalDate>,
        selectionMode: SelectionMode,
        override val pagerState: EpicCalendarPager.State
    ) : State {
        override val selectedDays = selectedDays.toMutableStateList()
        override var selectionMode by mutableStateOf(selectionMode)

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

        override fun toggleDaySelection(day: LocalDate) {
            val isRemoved = selectedDays.removeIf { it == day }
            if (isRemoved) return

            when (val mode = selectionMode) {
                is SelectionMode.Single -> {
                    selectedDays.clear()
                    selectedDays.add(day)
                }
                is SelectionMode.Range -> {
                    if (selectedDays.size == 2) {
                        selectedDays.clear()
                    }
                    selectedDays.add(day)
                }
                is SelectionMode.Multi -> {
                    if (selectedDays.size == mode.maxSize) {
                        selectedDays.removeFirst()
                    }
                    selectedDays.add(day)
                }
                else -> {
                    error("Unexpected selectionMode: $mode")
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
        val selectedDays: List<LocalDate>
        var selectionMode: SelectionMode
        val pagerState: EpicCalendarPager.State
        var displayDaysOfAdjacentMonths: Boolean
        var displayDaysOfWeek: Boolean
        fun toggleDaySelection(day: LocalDate)
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