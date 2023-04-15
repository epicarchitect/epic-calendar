package epicarchitect.epic.calendar.compose.lib

import androidx.compose.runtime.Immutable
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.*

@Immutable
enum class EpicDayOfWeek {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;

    val value = ordinal + 1;

    fun localized() =
        DayOfWeek.values()[ordinal].getDisplayName(TextStyle.SHORT, Locale.getDefault())!!

    companion object {
        fun firstDayOfWeekByCurrentLocale() =
            WeekFields.of(Locale.getDefault()).firstDayOfWeek.toEpic()

        fun entriesSortedByFirstDayOfWeek(firstDayOfWeek: EpicDayOfWeek) =
            DayOfWeek.values().map(DayOfWeek::toEpic).let {
                val n = 7 - firstDayOfWeek.ordinal
                it.takeLast(n) + it.dropLast(n)
            }
    }
}


internal fun DayOfWeek.toEpic() = when (this) {
    DayOfWeek.MONDAY -> EpicDayOfWeek.MONDAY
    DayOfWeek.TUESDAY -> EpicDayOfWeek.TUESDAY
    DayOfWeek.WEDNESDAY -> EpicDayOfWeek.WEDNESDAY
    DayOfWeek.THURSDAY -> EpicDayOfWeek.THURSDAY
    DayOfWeek.FRIDAY -> EpicDayOfWeek.FRIDAY
    DayOfWeek.SATURDAY -> EpicDayOfWeek.SATURDAY
    DayOfWeek.SUNDAY -> EpicDayOfWeek.SUNDAY
}

fun EpicDayOfWeek.countDaysToEndOfWeek(
    firstDayOfWeek: EpicDayOfWeek = EpicDayOfWeek.firstDayOfWeekByCurrentLocale()
) = when (firstDayOfWeek) {
    EpicDayOfWeek.MONDAY -> value - 1
    EpicDayOfWeek.SUNDAY -> {
        if (this == EpicDayOfWeek.SATURDAY) 0
        else value - 2
    }
    else -> error("Unexpected firstDayOfWeek: $firstDayOfWeek")
}

fun EpicDayOfWeek.index(
    firstDayOfWeek: EpicDayOfWeek = EpicDayOfWeek.firstDayOfWeekByCurrentLocale()
) = when (firstDayOfWeek) {
    EpicDayOfWeek.MONDAY -> value - 1
    EpicDayOfWeek.SUNDAY -> {
        if (this == EpicDayOfWeek.SATURDAY) 0
        else value + 1
    }
    else -> error("Unexpected firstDayOfWeek: $firstDayOfWeek")
}