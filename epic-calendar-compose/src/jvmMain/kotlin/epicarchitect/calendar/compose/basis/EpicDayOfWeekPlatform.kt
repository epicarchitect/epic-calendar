package epicarchitect.calendar.compose.basis

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toJavaDayOfWeek
import kotlinx.datetime.toKotlinDayOfWeek
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

actual fun firstDayOfWeekBySystem(): Int =
    WeekFields.of(Locale.getDefault()).firstDayOfWeek!!.toKotlinDayOfWeek().isoDayNumber

actual fun localizedBySystem(isoDayNumber: Int): String {
    val dayOfWeek = DayOfWeek(isoDayNumber).toJavaDayOfWeek()
    return dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())!!
}