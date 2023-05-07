package epicarchitect.calendar.compose.datepicker.config

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import epicarchitect.calendar.compose.pager.config.EpicCalendarPagerConfig

interface EpicDatePickerConfig {
    val pagerConfig: EpicCalendarPagerConfig
    val selectionContentColor: Color
    val selectionContainerColor: Color
}

val LocalEpicDatePickerConfig = compositionLocalOf<EpicDatePickerConfig> {
    DefaultEpicDatePickerConfig
}