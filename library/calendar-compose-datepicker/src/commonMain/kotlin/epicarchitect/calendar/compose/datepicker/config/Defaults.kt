package epicarchitect.calendar.compose.datepicker.config

import androidx.compose.ui.graphics.Color
import epicarchitect.calendar.compose.pager.config.DefaultEpicCalendarPagerConfig

val DefaultEpicDatePickerConfig = ImmutableEpicDatePickerConfig(
    pagerConfig = DefaultEpicCalendarPagerConfig,
    selectionContainerColor = Color.Red,
    selectionContentColor = Color.White
)