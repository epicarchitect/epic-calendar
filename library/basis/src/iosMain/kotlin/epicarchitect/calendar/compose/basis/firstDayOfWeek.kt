package epicarchitect.calendar.compose.basis

import kotlinx.datetime.DayOfWeek
import platform.Foundation.NSCalendar

private val localization = listOf(
    DayOfWeek.SUNDAY,
    DayOfWeek.MONDAY,
    DayOfWeek.TUESDAY,
    DayOfWeek.WEDNESDAY,
    DayOfWeek.THURSDAY,
    DayOfWeek.FRIDAY,
    DayOfWeek.SATURDAY
)

actual fun DayOfWeek.localized(): String {
    return NSCalendar.currentCalendar.veryShortWeekdaySymbols[localization.indexOf(this)].toString()
}

actual fun firstDayOfWeek(): DayOfWeek {
    return when (val day = NSCalendar.currentCalendar.firstWeekday) {
        1uL -> DayOfWeek.SUNDAY
        2uL -> DayOfWeek.MONDAY
        3uL -> DayOfWeek.TUESDAY
        4uL -> DayOfWeek.WEDNESDAY
        5uL -> DayOfWeek.THURSDAY
        6uL -> DayOfWeek.FRIDAY
        7uL -> DayOfWeek.SATURDAY
        else -> DayOfWeek.SUNDAY
    }
}