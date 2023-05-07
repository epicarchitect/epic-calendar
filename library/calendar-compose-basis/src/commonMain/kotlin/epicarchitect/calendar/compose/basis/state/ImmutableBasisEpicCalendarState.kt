package epicarchitect.calendar.compose.basis.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import epicarchitect.calendar.compose.basis.EpicMonth
import epicarchitect.calendar.compose.basis.calculateEpicCalendarGridInfo
import epicarchitect.calendar.compose.basis.firstDayOfWeek

@Immutable
class ImmutableBasisEpicCalendarState(
    override val currentMonth: EpicMonth,
    override val displayDaysOfAdjacentMonths: Boolean,
    override val displayDaysOfWeek: Boolean
) : BasisEpicCalendarState {
    private val firstDayOfWeek get() = firstDayOfWeek()

    override val dateGridInfo = calculateEpicCalendarGridInfo(
        currentMonth = currentMonth,
        displayDaysOfAdjacentMonths = this.displayDaysOfAdjacentMonths,
        firstDayOfWeek = firstDayOfWeek
    )
}

@Composable
fun rememberBasisEpicCalendarState(
    currentMonth: EpicMonth = LocalBasisEpicCalendarState.current?.currentMonth ?: EpicMonth.now(),
    displayDaysOfWeek: Boolean = LocalBasisEpicCalendarState.current?.displayDaysOfWeek ?: true,
    displayDaysOfAdjacentMonths: Boolean = LocalBasisEpicCalendarState.current?.displayDaysOfAdjacentMonths
        ?: true
): BasisEpicCalendarState = remember(
    currentMonth,
    displayDaysOfWeek,
    displayDaysOfAdjacentMonths
) {
    ImmutableBasisEpicCalendarState(
        currentMonth = currentMonth,
        displayDaysOfWeek = displayDaysOfWeek,
        displayDaysOfAdjacentMonths = displayDaysOfAdjacentMonths
    )
}