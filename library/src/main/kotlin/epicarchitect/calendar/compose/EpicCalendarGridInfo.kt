package epicarchitect.calendar.compose

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

fun calculateEpicCalendarGridInfo(
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
    } % DayOfWeekAmount

    val daysAmountInCurrentMonth = currentMonth.numberOfDays
    val firstDaysAmountInNextMonth =
        (GridCellAmount - lastDaysAmountInPreviousMonth - daysAmountInCurrentMonth).let {
            if (displayDaysOfAdjacentMonths) it
            else it % DayOfWeekAmount
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
        dateMatrix = dates.chunked(DayOfWeekAmount),
        firstDayOfWeek = firstDayOfWeek,
        currentMonth = currentMonth,
        previousMonth = previousMonth,
        nextMonth = nextMonth,
        daysOfWeek = EpicDayOfWeek.entriesSortedByFirstDayOfWeek(firstDayOfWeek)
    )
}

private const val GridCellAmount = 42
private const val DayOfWeekAmount = 7