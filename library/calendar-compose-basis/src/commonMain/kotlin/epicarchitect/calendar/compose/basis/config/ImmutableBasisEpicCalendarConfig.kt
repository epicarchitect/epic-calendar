package epicarchitect.calendar.compose.basis.config

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

@Immutable
data class ImmutableBasisEpicCalendarConfig(
    override val rowsSpacerHeight: Dp,
    override val dayOfWeekViewHeight: Dp,
    override val dayOfMonthViewHeight: Dp,
    override val columnWidth: Dp,
    override val dayOfWeekViewShape: Shape,
    override val dayOfMonthViewShape: Shape,
    override val contentPadding: PaddingValues,
    override val contentColor: Color
) : BasisEpicCalendarConfig

@Composable
fun rememberBasisEpicCalendarConfig(
    rowsSpacerHeight: Dp = LocalBasisEpicCalendarConfig.current.rowsSpacerHeight,
    dayOfWeekViewHeight: Dp = LocalBasisEpicCalendarConfig.current.dayOfWeekViewHeight,
    dayOfMonthViewHeight: Dp = LocalBasisEpicCalendarConfig.current.dayOfMonthViewHeight,
    columnWidth: Dp = LocalBasisEpicCalendarConfig.current.columnWidth,
    dayOfWeekViewShape: Shape = LocalBasisEpicCalendarConfig.current.dayOfWeekViewShape,
    dayOfMonthViewShape: Shape = LocalBasisEpicCalendarConfig.current.dayOfMonthViewShape,
    contentPadding: PaddingValues = LocalBasisEpicCalendarConfig.current.contentPadding,
    contentColor: Color = LocalBasisEpicCalendarConfig.current.contentColor
): BasisEpicCalendarConfig = remember(
    rowsSpacerHeight,
    dayOfWeekViewHeight,
    dayOfMonthViewHeight,
    columnWidth,
    dayOfWeekViewShape,
    dayOfMonthViewShape,
    contentPadding,
    contentColor
) {
    ImmutableBasisEpicCalendarConfig(
        rowsSpacerHeight = rowsSpacerHeight,
        dayOfWeekViewHeight = dayOfWeekViewHeight,
        dayOfMonthViewHeight = dayOfMonthViewHeight,
        columnWidth = columnWidth,
        dayOfWeekViewShape = dayOfWeekViewShape,
        dayOfMonthViewShape = dayOfMonthViewShape,
        contentPadding = contentPadding,
        contentColor = contentColor
    )
}