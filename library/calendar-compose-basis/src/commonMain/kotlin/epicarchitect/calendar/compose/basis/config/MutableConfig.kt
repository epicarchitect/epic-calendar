package epicarchitect.calendar.compose.basis.config

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import kotlinx.datetime.DayOfWeek

class MutableBasisEpicCalendarConfig(
    rowsSpacerHeight: Dp,
    dayOfWeekViewHeight: Dp,
    dayOfMonthViewHeight: Dp,
    columnWidth: Dp,
    dayOfWeekViewShape: Shape,
    dayOfMonthViewShape: Shape,
    contentPadding: PaddingValues,
    contentColor: Color,
    displayDaysOfAdjacentMonths: Boolean,
    displayDaysOfWeek: Boolean,
    daysOfWeek: List<DayOfWeek>
) : BasisEpicCalendarConfig {
    override var rowsSpacerHeight by mutableStateOf(rowsSpacerHeight)
    override var dayOfWeekViewHeight by mutableStateOf(dayOfWeekViewHeight)
    override var dayOfMonthViewHeight by mutableStateOf(dayOfMonthViewHeight)
    override var columnWidth by mutableStateOf(columnWidth)
    override var dayOfWeekViewShape by mutableStateOf(dayOfWeekViewShape)
    override var dayOfMonthViewShape by mutableStateOf(dayOfMonthViewShape)
    override var contentPadding by mutableStateOf(contentPadding)
    override var contentColor by mutableStateOf(contentColor)
    override var displayDaysOfAdjacentMonths by mutableStateOf(displayDaysOfAdjacentMonths)
    override var displayDaysOfWeek by mutableStateOf(displayDaysOfWeek)
    override var daysOfWeek by mutableStateOf(daysOfWeek)
}

@Composable
fun rememberMutableBasisEpicCalendarConfig(
    rowsSpacerHeight: Dp = LocalBasisEpicCalendarConfig.current.rowsSpacerHeight,
    dayOfWeekViewHeight: Dp = LocalBasisEpicCalendarConfig.current.dayOfWeekViewHeight,
    dayOfMonthViewHeight: Dp = LocalBasisEpicCalendarConfig.current.dayOfMonthViewHeight,
    columnWidth: Dp = LocalBasisEpicCalendarConfig.current.columnWidth,
    dayOfWeekViewShape: Shape = LocalBasisEpicCalendarConfig.current.dayOfWeekViewShape,
    dayOfMonthViewShape: Shape = LocalBasisEpicCalendarConfig.current.dayOfMonthViewShape,
    contentPadding: PaddingValues = LocalBasisEpicCalendarConfig.current.contentPadding,
    contentColor: Color = LocalBasisEpicCalendarConfig.current.contentColor,
    displayDaysOfAdjacentMonths: Boolean = LocalBasisEpicCalendarConfig.current.displayDaysOfAdjacentMonths,
    displayDaysOfWeek: Boolean = LocalBasisEpicCalendarConfig.current.displayDaysOfWeek,
    daysOfWeek: List<DayOfWeek> = LocalBasisEpicCalendarConfig.current.daysOfWeek,
): MutableBasisEpicCalendarConfig = remember(
    rowsSpacerHeight,
    dayOfWeekViewHeight,
    dayOfMonthViewHeight,
    columnWidth,
    dayOfWeekViewShape,
    dayOfMonthViewShape,
    contentPadding,
    contentColor,
    displayDaysOfAdjacentMonths,
    displayDaysOfWeek,
    daysOfWeek
) {
    MutableBasisEpicCalendarConfig(
        rowsSpacerHeight = rowsSpacerHeight,
        dayOfWeekViewHeight = dayOfWeekViewHeight,
        dayOfMonthViewHeight = dayOfMonthViewHeight,
        columnWidth = columnWidth,
        dayOfWeekViewShape = dayOfWeekViewShape,
        dayOfMonthViewShape = dayOfMonthViewShape,
        contentPadding = contentPadding,
        contentColor = contentColor,
        displayDaysOfAdjacentMonths = displayDaysOfAdjacentMonths,
        displayDaysOfWeek = displayDaysOfWeek,
        daysOfWeek = daysOfWeek
    )
}