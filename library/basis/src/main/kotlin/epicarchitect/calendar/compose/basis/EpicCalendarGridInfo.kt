package epicarchitect.calendar.compose.basis

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate

@Immutable
data class EpicCalendarGridInfo(
    val daysOfWeek: List<EpicDayOfWeek>,
    val dateMatrix: List<List<LocalDate>>,
    val currentMonth: EpicMonth,
    val previousMonth: EpicMonth,
    val nextMonth: EpicMonth,
    val firstDayOfWeek: EpicDayOfWeek
)

internal fun calculateEpicCalendarGridInfo(
    currentMonth: EpicMonth,
    displayDaysOfAdjacentMonths: Boolean,
    firstDayOfWeek: EpicDayOfWeek
): EpicCalendarGridInfo {
    val previousMonth = currentMonth.previous()
    val nextMonth = currentMonth.next()
    val previousMonthLastDayOfWeek = previousMonth.lastDayOfWeek()

    val lastDaysAmountInPreviousMonth = when (firstDayOfWeek) {
        EpicDayOfWeek.MONDAY -> previousMonthLastDayOfWeek.value
        EpicDayOfWeek.SUNDAY -> {
            if (previousMonthLastDayOfWeek == EpicDayOfWeek.SATURDAY) 0
            else previousMonthLastDayOfWeek.value + 1
        }

        else -> error("Unexpected firstDayOfWeek: $firstDayOfWeek")
    } % EpicCalendarConstants.DayOfWeekAmount

    val daysAmountInCurrentMonth = currentMonth.numberOfDays
    val firstDaysAmountInNextMonth =
        (EpicCalendarConstants.GridCellAmount - lastDaysAmountInPreviousMonth - daysAmountInCurrentMonth).let {
            if (displayDaysOfAdjacentMonths) it
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
        firstDayOfWeek = firstDayOfWeek,
        currentMonth = currentMonth,
        previousMonth = previousMonth,
        nextMonth = nextMonth,
        daysOfWeek = EpicDayOfWeek.entriesSortedByFirstDayOfWeek(firstDayOfWeek)
    )
}