package epicarchitect.calendar.compose.basis

import kotlinx.datetime.DayOfWeek

actual fun DayOfWeek.localizedBySystem(): String = when (this) {
    DayOfWeek.MONDAY -> "Mon"
    DayOfWeek.TUESDAY -> "Tue"
    DayOfWeek.WEDNESDAY -> "Wed"
    DayOfWeek.THURSDAY -> "Thu"
    DayOfWeek.FRIDAY -> "Fri"
    DayOfWeek.SATURDAY -> "Sat"
    DayOfWeek.SUNDAY -> "Sun"
}

actual fun firstDayOfWeekBySystem(): DayOfWeek = DayOfWeek.MONDAY
