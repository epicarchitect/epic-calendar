package epicarchitect.calendar.compose.basis

import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale
import kotlinx.datetime.DayOfWeek
import java.time.DayOfWeek as JavaDayOfWeek

actual fun firstDayOfWeekBySystem(): DayOfWeek =
    DayOfWeek.valueOf(WeekFields.of(Locale.getDefault()).firstDayOfWeek.name)

actual fun DayOfWeek.localizedBySystem(): String =
    JavaDayOfWeek.valueOf(name).getDisplayName(TextStyle.SHORT, Locale.getDefault())
