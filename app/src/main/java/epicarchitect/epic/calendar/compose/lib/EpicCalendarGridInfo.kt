package epicarchitect.epic.calendar.compose.lib

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate

@Immutable
class EpicCalendarGridInfo(
    val daysOfWeek: List<EpicDayOfWeek>,
    val dateMatrix: List<List<LocalDate>>,
    val dateInfoMap: Map<LocalDate, DateInfo>,
    val previousMonth: EpicMonth,
    val nextMonth: EpicMonth,
    val currentMonth: EpicMonth,
    val firstDayOfWeek: EpicDayOfWeek,
    val maxPosition: EpicGridPosition
) {
    @Immutable
    data class DateInfo(
        val month: EpicMonth,
        val position: EpicGridPosition
    )
}

@Immutable
data class EpicGridPosition(
    val row: Int,
    val column: Int
)

fun calculateEpicDateGridInfo(
    currentMonth: EpicMonth,
    displayDaysOfAdjacentMonths: Boolean,
    firstDayOfWeek: EpicDayOfWeek
): EpicCalendarGridInfo {
    val previousMonth = currentMonth.previous()
    val nextMonth = currentMonth.next()
    val previousMonthLastDayOfWeek = previousMonth.lastDayOfWeek()

    val countLastDaysInPreviousMonth = when (firstDayOfWeek) {
        EpicDayOfWeek.MONDAY -> previousMonthLastDayOfWeek.value
        EpicDayOfWeek.SUNDAY -> {
            if (previousMonthLastDayOfWeek == EpicDayOfWeek.SATURDAY) 0
            else previousMonthLastDayOfWeek.value + 1
        }

        else -> error("Unexpected firstDayOfWeek: $firstDayOfWeek")
    } % 7

    val countDaysInCurrentMonth = currentMonth.numberOfDays
    val countFirstDaysInNextMonth =
        (42 - countLastDaysInPreviousMonth - countDaysInCurrentMonth).let {
            if (displayDaysOfAdjacentMonths) it
            else it % 7
        }

    val dates = mutableListOf<LocalDate>()
    val daysInfo = mutableMapOf<LocalDate, EpicCalendarGridInfo.DateInfo>()
    var maxPosition: EpicGridPosition? = null

    repeat(countLastDaysInPreviousMonth) {
        val date = previousMonth.atDay(
            previousMonth.numberOfDays + it + 1 - countLastDaysInPreviousMonth
        )
        val position = EpicGridPosition(
            row = it / 7,
            column = it % 7
        )
        val info = EpicCalendarGridInfo.DateInfo(
            month = previousMonth,
            position = position
        )
        daysInfo[date] = info
        dates.add(date)
        maxPosition = position
    }

    repeat(countDaysInCurrentMonth) {
        val date = currentMonth.atDay(it + 1)
        val position = EpicGridPosition(
            row = (it + countLastDaysInPreviousMonth) / 7,
            column = (it + countLastDaysInPreviousMonth) % 7
        )
        val info = EpicCalendarGridInfo.DateInfo(
            month = currentMonth,
            position = position
        )
        daysInfo[date] = info
        dates.add(date)
        maxPosition = position
    }

    repeat(countFirstDaysInNextMonth) {
        val date = nextMonth.atDay(it + 1)
        val position = EpicGridPosition(
            row = (it + countLastDaysInPreviousMonth + countDaysInCurrentMonth) / 7,
            column = (it + countLastDaysInPreviousMonth + countDaysInCurrentMonth) % 7
        )
        val info = EpicCalendarGridInfo.DateInfo(
            month = nextMonth,
            position = position
        )
        daysInfo[date] = info
        dates.add(date)
        maxPosition = position
    }

    return EpicCalendarGridInfo(
        dateMatrix = dates.chunked(7),
        dateInfoMap = daysInfo,
        previousMonth = previousMonth,
        nextMonth = nextMonth,
        currentMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek,
        daysOfWeek = EpicDayOfWeek.entriesSortedByFirstDayOfWeek(firstDayOfWeek),
        maxPosition = maxPosition!!
    )
}
