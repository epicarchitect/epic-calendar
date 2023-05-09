package epicarchitect.sample.pagerTesting

import epicarchitect.sample.PrevNextButtons
import androidx.compose.runtime.Composable
import epicarchitect.calendar.compose.pager.state.EpicCalendarPagerState

@Composable
fun PagerStateControls(state: EpicCalendarPagerState) {
    PrevNextButtons(
        onPrev = { state.scrollMonths(-1) },
        onNext = { state.scrollMonths(1) },
        text = state.currentMonth.month.name
    )

    PrevNextButtons(
        onPrev = { state.scrollYears(-1) },
        onNext = { state.scrollYears(1) },
        text = state.currentMonth.year.toString()
    )
}