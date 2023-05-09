package epicarchitect.calendar.compose.pager.state

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.compositionLocalOf
import epicarchitect.calendar.compose.basis.EpicMonth
import epicarchitect.calendar.compose.pager.config.EpicCalendarPagerConfig

@OptIn(ExperimentalFoundationApi::class)
interface EpicCalendarPagerState {
    val config: EpicCalendarPagerConfig
    val currentMonth: EpicMonth
    var monthRange: ClosedRange<EpicMonth>
    val pagerState: PagerState
    suspend fun scrollToMonth(month: EpicMonth)
    suspend fun scrollYears(amount: Int)
    suspend fun scrollMonths(amount: Int)
}

val LocalEpicCalendarPagerState = compositionLocalOf<EpicCalendarPagerState?> {
    null
}