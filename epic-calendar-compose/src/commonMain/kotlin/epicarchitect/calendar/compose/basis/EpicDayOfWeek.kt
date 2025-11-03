package epicarchitect.calendar.compose.basis

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.isoDayNumber

expect fun localizedBySystem(isoDayNumber: Int): String

expect fun firstDayOfWeekBySystem(): Int

fun daysOfWeekSortedBy(
    firstDayOfWeek: DayOfWeek
) = DayOfWeek.entries.toTypedArray().let {
    val n = it.size - firstDayOfWeek.ordinal
    it.takeLast(n) + it.dropLast(n)
}

fun DayOfWeek.index(
    firstDayOfWeek: DayOfWeek
) = when (firstDayOfWeek) {
    DayOfWeek.MONDAY -> isoDayNumber - 1
    DayOfWeek.SUNDAY -> {
        if (this == DayOfWeek.SUNDAY) 0
        else isoDayNumber
    }

    else -> error("Unexpected firstDayOfWeek: $firstDayOfWeek")
}