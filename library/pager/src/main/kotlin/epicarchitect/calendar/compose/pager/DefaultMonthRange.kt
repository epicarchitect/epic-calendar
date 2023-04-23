package epicarchitect.calendar.compose.pager

import epicarchitect.calendar.compose.basis.EpicMonth
import kotlinx.datetime.TimeZone
import java.time.Month

internal fun defaultMonthRange() = EpicMonth(2000, Month.JANUARY)..EpicMonth.now(
    TimeZone.currentSystemDefault()
)