package epicarchitect.calendar.compose.pager.state

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.compositionLocalOf
import epicarchitect.calendar.compose.basis.EpicMonth

@OptIn(ExperimentalFoundationApi::class)
interface EpicCalendarPagerState {
    val currentMonth: EpicMonth
    var monthRange: ClosedRange<EpicMonth>
    var displayDaysOfAdjacentMonths: Boolean
    var displayDaysOfWeek: Boolean
    val pagerState: PagerState
    suspend fun scrollToMonth(month: EpicMonth)
    suspend fun scrollYears(amount: Int)
    suspend fun scrollMonths(amount: Int)
}

val LocalEpicCalendarPagerState = compositionLocalOf<EpicCalendarPagerState?> {
    null
}