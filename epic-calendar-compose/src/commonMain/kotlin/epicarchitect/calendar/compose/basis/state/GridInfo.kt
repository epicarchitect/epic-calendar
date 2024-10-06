package epicarchitect.calendar.compose.basis.state

import androidx.compose.runtime.Immutable
import epicarchitect.calendar.compose.basis.EpicCalendarConstants
import epicarchitect.calendar.compose.basis.EpicMonth
import epicarchitect.calendar.compose.basis.atDay
import epicarchitect.calendar.compose.basis.config.BasisEpicCalendarConfig
import epicarchitect.calendar.compose.basis.lastDayOfWeek
import epicarchitect.calendar.compose.basis.next
import epicarchitect.calendar.compose.basis.previous
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber

@Immutable
data class EpicCalendarGridInfo(
    val dateMatrix: List<List<LocalDate>>,
    val currentMonth: EpicMonth,
    val previousMonth: EpicMonth,
    val nextMonth: EpicMonth
)

internal fun calculateEpicCalendarGridInfo(
    currentMonth: EpicMonth,
    config: BasisEpicCalendarConfig,
): EpicCalendarGridInfo {
    val firstDayOfWeek = config.daysOfWeek.first()
    val previousMonth = currentMonth.previous()
    val nextMonth = currentMonth.next()
    val previousMonthLastDayOfWeek = previousMonth.lastDayOfWeek()

    val lastDaysAmountInPreviousMonth = when (firstDayOfWeek) {
        DayOfWeek.MONDAY -> previousMonthLastDayOfWeek.isoDayNumber
        DayOfWeek.SUNDAY -> {
            if (previousMonthLastDayOfWeek == DayOfWeek.SATURDAY) 0
            else previousMonthLastDayOfWeek.isoDayNumber + 1
        }

        else -> error("Unexpected firstDayOfWeek: $firstDayOfWeek")
    } % EpicCalendarConstants.DayOfWeekAmount

    val daysAmountInCurrentMonth = currentMonth.numberOfDays
    val firstDaysAmountInNextMonth =
        (EpicCalendarConstants.GridCellAmount - lastDaysAmountInPreviousMonth - daysAmountInCurrentMonth).let {
            if (config.displayDaysOfAdjacentMonths) it
            else it % EpicCalendarConstants.DayOfWeekAmount
        }

    val dates = mutableListOf<LocalDate>()

    repeat(lastDaysAmountInPreviousMonth) {
        dates.add(
            previousMonth.atDay(
                previousMonth.numberOfDays + it + 1 - lastDaysAmountInPreviousMonth
            )
        )
    }

    repeat(daysAmountInCurrentMonth) {
        dates.add(
            currentMonth.atDay(it + 1)
        )
    }

    repeat(firstDaysAmountInNextMonth) {
        dates.add(
            nextMonth.atDay(it + 1)
        )
    }

    return EpicCalendarGridInfo(
        dateMatrix = dates.chunked(EpicCalendarConstants.DayOfWeekAmount),
        currentMonth = currentMonth,
        previousMonth = previousMonth,
        nextMonth = nextMonth
    )
}