package epicarchitect.calendar.compose.basis.config

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import kotlinx.datetime.DayOfWeek

interface BasisEpicCalendarConfig {
    val rowsSpacerHeight: Dp
    val dayOfWeekViewHeight: Dp
    val dayOfMonthViewHeight: Dp
    val columnWidth: Dp
    val dayOfWeekShape: Shape
    val dayOfMonthShape: Shape
    val contentPadding: PaddingValues
    val contentColor: Color
    val displayDaysOfAdjacentMonths: Boolean
    val displayDaysOfWeek: Boolean
    val daysOfWeek: List<DayOfWeek>
}

val LocalBasisEpicCalendarConfig = compositionLocalOf<BasisEpicCalendarConfig> {
    DefaultBasisEpicCalendarConfig
}