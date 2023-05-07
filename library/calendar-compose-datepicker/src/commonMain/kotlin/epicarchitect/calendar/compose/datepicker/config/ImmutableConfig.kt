package epicarchitect.calendar.compose.datepicker.config

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import epicarchitect.calendar.compose.pager.config.EpicCalendarPagerConfig

@Immutable
data class ImmutableConfig(
    override val pagerConfig: EpicCalendarPagerConfig,
    override val selectionContentColor: Color,
    override val selectionContainerColor: Color
) : EpicDatePickerConfig