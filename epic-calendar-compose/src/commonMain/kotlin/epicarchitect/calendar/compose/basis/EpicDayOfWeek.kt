package epicarchitect.calendar.compose.basis


import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.isoDayNumber

expect fun DayOfWeek.localizedBySystem(): String

expect fun firstDayOfWeekBySystem(): DayOfWeek

fun daysOfWeekSortedBy(
    firstDayOfWeek: DayOfWeek
) = DayOfWeek.values().let {
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