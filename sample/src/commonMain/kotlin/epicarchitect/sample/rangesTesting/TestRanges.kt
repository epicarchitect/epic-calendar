package epicarchitect.sample.rangesTesting

import epicarchitect.calendar.compose.basis.EpicMonth
import epicarchitect.calendar.compose.basis.atStartDay
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus

val currentMonth: EpicMonth = EpicMonth.now()
val testRanges = listOf(
    currentMonth.atStartDay().let { it..it },
    currentMonth.atStartDay().let {
        it.plus(1, DateTimeUnit.DAY)..it.plus(3, DateTimeUnit.DAY)
    },
    currentMonth.atStartDay().let {
        it.plus(13, DateTimeUnit.DAY)..it.plus(55, DateTimeUnit.DAY)
    }
)