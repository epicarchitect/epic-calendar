package epicarchitect.calendar.compose.pager.state

import epicarchitect.calendar.compose.basis.EpicMonth
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone

internal fun defaultMonthRange() = EpicMonth(2000, Month.JANUARY)..EpicMonth.now(
    TimeZone.currentSystemDefault()
)