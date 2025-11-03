package epicarchitect.calendar.compose.basis

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.isoDayNumber
import kotlin.js.Date

private val epicDate = Date(year = 2023, month = 4, day = 1)

actual fun localizedBySystem(isoDayNumber: Int): String =
    Date(year = 2023, month = 4, day = isoDayNumber).toLocaleString(
        "weekday",
        dateLocaleOptions {
            weekday = "short"
        }
    )

actual fun firstDayOfWeekBySystem(): Int = when (epicDate.getDay()) {
    0 -> DayOfWeek.SUNDAY.isoDayNumber
    1 -> DayOfWeek.MONDAY.isoDayNumber
    2 -> DayOfWeek.TUESDAY.isoDayNumber
    3 -> DayOfWeek.WEDNESDAY.isoDayNumber
    4 -> DayOfWeek.THURSDAY.isoDayNumber
    5 -> DayOfWeek.FRIDAY.isoDayNumber
    6 -> DayOfWeek.SATURDAY.isoDayNumber
    else -> DayOfWeek.SUNDAY.isoDayNumber
}
