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
import epicarchitect.calendar.compose.basis.size
import epicarchitect.calendar.compose.pager.config.EpicCalendarPagerConfig
import epicarchitect.calendar.compose.pager.config.LocalEpicCalendarPagerConfig

@OptIn(ExperimentalFoundationApi::class)
class DefaultEpicCalendarPagerState(
    config: EpicCalendarPagerConfig,
    monthRange: ClosedRange<EpicMonth>,
    override val pagerState: PagerState
) : EpicCalendarPagerState {
    override var config by mutableStateOf(config)
    override var monthRange by mutableStateOf(monthRange)
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
    config: EpicCalendarPagerConfig = LocalEpicCalendarPagerConfig.current,
    monthRange: ClosedRange<EpicMonth> = defaultEpicCalendarPagerMonthRange(),
    initialMonth: EpicMonth = EpicMonth.now(),
): DefaultEpicCalendarPagerState {
    val pagerState = rememberPagerState(
        initialPage = remember(monthRange, initialMonth) {
            monthRange.indexOf(initialMonth) ?: 0
        },
        initialPageOffsetFraction = 0f,
        pageCount = {monthRange.size()}
    )

    return remember(
        config,
        monthRange,
        initialMonth,
        pagerState,
    ) {
        DefaultEpicCalendarPagerState(
            config = config,
            pagerState = pagerState,
            monthRange = monthRange
        )
    }
}