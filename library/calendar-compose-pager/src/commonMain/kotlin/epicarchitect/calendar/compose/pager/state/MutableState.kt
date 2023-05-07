package epicarchitect.calendar.compose.pager.state

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import epicarchitect.calendar.compose.basis.EpicMonth
import epicarchitect.calendar.compose.basis.addMonths
import epicarchitect.calendar.compose.basis.addYears
import epicarchitect.calendar.compose.basis.getByIndex
import epicarchitect.calendar.compose.basis.indexOf

@OptIn(ExperimentalFoundationApi::class)
class DefaultEpicCalendarPagerState(
    monthRange: ClosedRange<EpicMonth>,
    displayDaysOfAdjacentMonths: Boolean,
    displayDaysOfWeek: Boolean,
    override val pagerState: PagerState
) : EpicCalendarPagerState {
    override var monthRange by mutableStateOf(monthRange)
    override var displayDaysOfAdjacentMonths by mutableStateOf(displayDaysOfAdjacentMonths)
    override var displayDaysOfWeek by mutableStateOf(displayDaysOfWeek)
    override val currentMonth get() = monthRange.getByIndex(pagerState.currentPage)

    override suspend fun scrollToMonth(month: EpicMonth) {
        monthRange.indexOf(month)?.let {
            pagerState.animateScrollToPage(it)
        }
    }

    override suspend fun scrollYears(amount: Int) =
        scrollToMonth(currentMonth.addYears(amount))

    override suspend fun scrollMonths(amount: Int) =
        scrollToMonth(currentMonth.addMonths(amount))
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberEpicCalendarPagerState(
    monthRange: ClosedRange<EpicMonth> = defaultMonthRange(),
    initialMonth: EpicMonth = monthRange.start,
    displayDaysOfAdjacentMonths: Boolean = true,
    displayDaysOfWeek: Boolean = true,
): DefaultEpicCalendarPagerState {
    val pagerState = rememberPagerState(
        initialPage = remember(monthRange, initialMonth) {
            monthRange.indexOf(initialMonth) ?: 0
        }
    )

    return remember(
        monthRange,
        initialMonth,
        displayDaysOfAdjacentMonths,
        displayDaysOfWeek,
        pagerState,
    ) {
        DefaultEpicCalendarPagerState(
            pagerState = pagerState,
            displayDaysOfAdjacentMonths = displayDaysOfAdjacentMonths,
            displayDaysOfWeek = displayDaysOfWeek,
            monthRange = monthRange
        )
    }
}