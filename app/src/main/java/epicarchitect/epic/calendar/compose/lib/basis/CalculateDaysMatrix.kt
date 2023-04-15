package epicarchitect.epic.calendar.compose.lib.basis

import epicarchitect.epic.calendar.compose.lib.EpicDayOfWeek
import epicarchitect.epic.calendar.compose.lib.EpicMonth
import epicarchitect.epic.calendar.compose.lib.atDay
import epicarchitect.epic.calendar.compose.lib.lastDayOfWeek
import epicarchitect.epic.calendar.compose.lib.length
import epicarchitect.epic.calendar.compose.lib.next
import epicarchitect.epic.calendar.compose.lib.previous
import kotlinx.datetime.LocalDate

fun calculateDaysMatrix(
    currentMonth: EpicMonth,
    displayDaysOfAdjacentMonths: Boolean,
    firstDayOfWeek: EpicDayOfWeek
): List<List<LocalDate?>> {
    val previousYearMonth = currentMonth.previous()
    val nextYearMonth = currentMonth.next()
    val previousMonthLastDayOfWeek = previousYearMonth.lastDayOfWeek()

    val countLastDaysInPreviousMonth = when (firstDayOfWeek) {
        EpicDayOfWeek.MONDAY -> previousMonthLastDayOfWeek.value
        EpicDayOfWeek.SUNDAY -> {
            if (previousMonthLastDayOfWeek == EpicDayOfWeek.SATURDAY) 0
            else previousMonthLastDayOfWeek.value + 1
        }
        else -> error("Unexpected firstDayOfWeek: $firstDayOfWeek")
    }
    val countDaysInCurrentMonth = currentMonth.length()
    val countFirstDaysInNextMonth = 42 - countLastDaysInPreviousMonth - countDaysInCurrentMonth

    val days = mutableListOf<LocalDate?>()

    repeat(countLastDaysInPreviousMonth) {
        days.add(
            if (displayDaysOfAdjacentMonths.not()) null
            else previousYearMonth.atDay(
                previousYearMonth.length() + it + 1 - countLastDaysInPreviousMonth
            )
        )
    }

    repeat(countDaysInCurrentMonth) {
        days.add(
            currentMonth.atDay(it + 1)
        )
    }

    repeat(countFirstDaysInNextMonth) {
        days.add(
            if (displayDaysOfAdjacentMonths.not()) null
            else nextYearMonth.atDay(it + 1)
        )
    }

    return days.chunked(7).filterNot { row ->
        row.all { it == null }
    }
}