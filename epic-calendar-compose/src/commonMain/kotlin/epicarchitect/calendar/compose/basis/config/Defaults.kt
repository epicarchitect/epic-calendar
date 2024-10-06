package epicarchitect.calendar.compose.basis.config

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import epicarchitect.calendar.compose.basis.daysOfWeekSortedBy
import epicarchitect.calendar.compose.basis.firstDayOfWeekBySystem

fun defaultBasisEpicCalendarConfig() = ImmutableBasisEpicCalendarConfig(
    rowsSpacerHeight = 4.dp,
    dayOfWeekViewHeight = 40.dp,
    dayOfMonthViewHeight = 40.dp,
    columnWidth = 40.dp,
    dayOfWeekShape = RoundedCornerShape(16.dp),
    dayOfMonthShape = RoundedCornerShape(16.dp),
    contentPadding = PaddingValues(0.dp),
    contentColor = Color.Unspecified,
    displayDaysOfAdjacentMonths = true,
    displayDaysOfWeek = true,
    daysOfWeek = daysOfWeekSortedBy(firstDayOfWeekBySystem())
)