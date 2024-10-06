package epicarchitect.calendar.compose.basis.config

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import kotlinx.datetime.DayOfWeek

@Immutable
data class ImmutableBasisEpicCalendarConfig(
    override val rowsSpacerHeight: Dp,
    override val dayOfWeekViewHeight: Dp,
    override val dayOfMonthViewHeight: Dp,
    override val columnWidth: Dp,
    override val dayOfWeekShape: Shape,
    override val dayOfMonthShape: Shape,
    override val contentPadding: PaddingValues,
    override val contentColor: Color,
    override val displayDaysOfAdjacentMonths: Boolean,
    override val displayDaysOfWeek: Boolean,
    override val daysOfWeek: List<DayOfWeek>
) : BasisEpicCalendarConfig

@Stable
@Composable
fun rememberBasisEpicCalendarConfig(
    rowsSpacerHeight: Dp = LocalBasisEpicCalendarConfig.current.rowsSpacerHeight,
    dayOfWeekViewHeight: Dp = LocalBasisEpicCalendarConfig.current.dayOfWeekViewHeight,
    dayOfMonthViewHeight: Dp = LocalBasisEpicCalendarConfig.current.dayOfMonthViewHeight,
    columnWidth: Dp = LocalBasisEpicCalendarConfig.current.columnWidth,
    dayOfWeekViewShape: Shape = LocalBasisEpicCalendarConfig.current.dayOfWeekShape,
    dayOfMonthViewShape: Shape = LocalBasisEpicCalendarConfig.current.dayOfMonthShape,
    contentPadding: PaddingValues = LocalBasisEpicCalendarConfig.current.contentPadding,
    contentColor: Color = LocalBasisEpicCalendarConfig.current.contentColor,
    displayDaysOfAdjacentMonths: Boolean = LocalBasisEpicCalendarConfig.current.displayDaysOfAdjacentMonths,
    displayDaysOfWeek: Boolean = LocalBasisEpicCalendarConfig.current.displayDaysOfWeek,
    daysOfWeek: List<DayOfWeek> = LocalBasisEpicCalendarConfig.current.daysOfWeek
): BasisEpicCalendarConfig = remember(
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
    ImmutableBasisEpicCalendarConfig(
        rowsSpacerHeight = rowsSpacerHeight,
        dayOfWeekViewHeight = dayOfWeekViewHeight,
        dayOfMonthViewHeight = dayOfMonthViewHeight,
        columnWidth = columnWidth,
        dayOfWeekShape = dayOfWeekViewShape,
        dayOfMonthShape = dayOfMonthViewShape,
        contentPadding = contentPadding,
        contentColor = contentColor,
        displayDaysOfAdjacentMonths = displayDaysOfAdjacentMonths,
        displayDaysOfWeek = displayDaysOfWeek,
        daysOfWeek = daysOfWeek
    )
}