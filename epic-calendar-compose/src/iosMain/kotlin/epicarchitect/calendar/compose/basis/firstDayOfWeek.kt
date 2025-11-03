package epicarchitect.calendar.compose.basis

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.isoDayNumber
import platform.Foundation.NSCalendar

actual fun localizedBySystem(isoDayNumber: Int): String {
    val dayOfWeek = DayOfWeek(isoDayNumber)
    return NSCalendar.currentCalendar.veryShortWeekdaySymbols[dayOfWeek.index(firstDayOfWeek = DayOfWeek.SUNDAY)].toString()
}

actual fun firstDayOfWeekBySystem(): Int = when (NSCalendar.currentCalendar.firstWeekday) {
    1uL -> DayOfWeek.SUNDAY.isoDayNumber
    2uL -> DayOfWeek.MONDAY.isoDayNumber
    3uL -> DayOfWeek.TUESDAY.isoDayNumber
    4uL -> DayOfWeek.WEDNESDAY.isoDayNumber
    5uL -> DayOfWeek.THURSDAY.isoDayNumber
    6uL -> DayOfWeek.FRIDAY.isoDayNumber
    7uL -> DayOfWeek.SATURDAY.isoDayNumber
    else -> DayOfWeek.SUNDAY.isoDayNumber
}