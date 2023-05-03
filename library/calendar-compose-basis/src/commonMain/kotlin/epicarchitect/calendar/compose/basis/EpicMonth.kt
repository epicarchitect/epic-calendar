package epicarchitect.calendar.compose.basis

import androidx.compose.runtime.Immutable
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs
import kotlin.math.floor

@Immutable
data class EpicMonth(
    val year: Int,
    val month: Month
) : Comparable<EpicMonth> {

    val numberOfDays = length(isLeapYear(year.toLong()))

    private fun length(leapYear: Boolean): Int {
        return when (month) {
            Month.FEBRUARY -> if (leapYear) 29 else 28
            Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
            else -> 31
        }
    }

    override fun compareTo(other: EpicMonth): Int {
        var cmp = year - other.year
        if (cmp == 0) {
            cmp = month.number - other.month.number
        }
        return cmp
    }

    companion object {
        fun now(timeZone: TimeZone = TimeZone.currentSystemDefault()) =
            Clock.System.now().toLocalDateTime(timeZone).date.epicMonth
    }
}

fun EpicMonth.addYears(amount: Int) = copy(year = year + amount)

fun EpicMonth.addMonths(monthsToAdd: Int): EpicMonth {
    if (monthsToAdd == 0) return this
    val monthCount = year * 12 + (month.number - 1)
    val calcMonths = monthCount + monthsToAdd
    val newYear = floor(calcMonths.div(12.0)).toInt()
    val newMonth = floor(calcMonths.mod(12).toDouble()).toInt() + 1
    return EpicMonth(newYear, Month(newMonth))
}

fun EpicMonth.previous(): EpicMonth = addMonths(-1)

fun EpicMonth.next(): EpicMonth = addMonths(1)

val LocalDate.epicMonth: EpicMonth get() = EpicMonth(year, month)

fun EpicMonth.atDay(dayOfMonth: Int) = LocalDate(year, month, dayOfMonth)

fun EpicMonth.atStartDay() = atDay(1)

fun EpicMonth.atEndDay() = atDay(numberOfDays)

fun EpicMonth.lastDayOfWeek() = atDay(numberOfDays).dayOfWeek

// copied from IsoChronology
fun isLeapYear(year: Long): Boolean {
    return year and 3L == 0L && (year % 100 != 0L || year % 400 == 0L)
}

operator fun EpicMonth.contains(date: LocalDate): Boolean {
    val start = LocalDateTime(year, month, 1, 0, 0, 0).date
    val end = LocalDateTime(year, month, numberOfDays, 23, 59, 59).date
    return date in start..end
}

fun ClosedRange<EpicMonth>.getByIndex(index: Int) = start.addMonths(
    if (start < endInclusive) index
    else -index
)

fun ClosedRange<EpicMonth>.indexOf(month: EpicMonth): Int? {
    val (start, end) = if (start < endInclusive) start to month else endInclusive to month
    val startMonthCount = start.year * 12 + start.month.number
    val endMonthCount = end.year * 12 + end.month.number
    val result = endMonthCount - startMonthCount
    return if (result < 0 || result >= size()) null else result
}

fun ClosedRange<EpicMonth>.size(): Int {
    val startMonthCount = start.year * 12 + start.month.number
    val endMonthCount = endInclusive.year * 12 + endInclusive.month.number
    return abs(endMonthCount - startMonthCount) + 1
}

fun main() {
    val range = EpicMonth(2000, Month.JANUARY)..EpicMonth(2001, Month.JANUARY)
    val r = range.indexOf(EpicMonth(2001, Month.FEBRUARY))
    println(r.toString())
}
