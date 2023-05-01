package epicarchitect.calendar.compose.basis

import kotlinx.datetime.DayOfWeek
import kotlin.js.Date

actual fun DayOfWeek.localized(): String {
    return Date(year = 2023, month = 4, day = ordinal + 1).toLocaleString(
        "weekday",
        dateLocaleOptions {
            weekday = "short"
        }
    )
}

actual fun firstDayOfWeek(): DayOfWeek {
    val epicDate = Date(year = 2023, month = 4, day = 1)
    return when (epicDate.getDay()) {
        0 -> DayOfWeek.SUNDAY
        1 -> DayOfWeek.MONDAY
        2 -> DayOfWeek.TUESDAY
        3 -> DayOfWeek.WEDNESDAY
        4 -> DayOfWeek.THURSDAY
        5 -> DayOfWeek.FRIDAY
        6 -> DayOfWeek.SATURDAY
        else -> DayOfWeek.SUNDAY
    }
}