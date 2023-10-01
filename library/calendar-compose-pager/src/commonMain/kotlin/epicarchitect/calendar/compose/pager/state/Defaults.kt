package epicarchitect.calendar.compose.pager.state

import epicarchitect.calendar.compose.basis.EpicMonth
import kotlinx.datetime.Month

fun defaultEpicCalendarPagerMonthRange() =
    EpicMonth(1900, Month.JANUARY)..EpicMonth(2100, Month.DECEMBER)