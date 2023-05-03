package epicarchitect.calendar.compose.basis

import java.time.DayOfWeek
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

actual fun firstDayOfWeek(): DayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek!!

actual fun DayOfWeek.localized(): String = getDisplayName(TextStyle.SHORT, Locale.getDefault())!!