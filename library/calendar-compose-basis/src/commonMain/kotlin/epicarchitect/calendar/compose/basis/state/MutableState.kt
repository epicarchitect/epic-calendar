package epicarchitect.calendar.compose.basis.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import epicarchitect.calendar.compose.basis.EpicMonth
import epicarchitect.calendar.compose.basis.firstDayOfWeek

class MutableBasisEpicCalendarState(
    override val currentMonth: EpicMonth,
    displayDaysOfAdjacentMonths: Boolean,
    displayDaysOfWeek: Boolean
) : BasisEpicCalendarState {
    override var displayDaysOfAdjacentMonths by mutableStateOf(displayDaysOfAdjacentMonths)
    override var displayDaysOfWeek by mutableStateOf(displayDaysOfWeek)

    private val firstDayOfWeek get() = firstDayOfWeek()

    override val dateGridInfo by derivedStateOf {
        calculateEpicCalendarGridInfo(
            currentMonth = currentMonth,
            displayDaysOfAdjacentMonths = this.displayDaysOfAdjacentMonths,
            firstDayOfWeek = firstDayOfWeek
        )
    }
}

@Composable
fun rememberMutableBasisEpicCalendarState(
    currentMonth: EpicMonth = LocalBasisEpicCalendarState.current?.currentMonth ?: EpicMonth.now(),
    displayDaysOfWeek: Boolean = LocalBasisEpicCalendarState.current?.displayDaysOfWeek ?: true,
    displayDaysOfAdjacentMonths: Boolean = LocalBasisEpicCalendarState.current?.displayDaysOfAdjacentMonths
        ?: true
): MutableBasisEpicCalendarState = remember(
    currentMonth,
    displayDaysOfWeek,
    displayDaysOfAdjacentMonths
) {
    MutableBasisEpicCalendarState(
        currentMonth = currentMonth,
        displayDaysOfWeek = displayDaysOfWeek,
        displayDaysOfAdjacentMonths = displayDaysOfAdjacentMonths
    )
}