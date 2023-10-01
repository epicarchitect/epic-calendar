package epicarchitect.calendar.compose.pager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import epicarchitect.calendar.compose.basis.BasisDayOfMonthContent
import epicarchitect.calendar.compose.basis.BasisDayOfWeekContent
import epicarchitect.calendar.compose.basis.BasisEpicCalendar
import epicarchitect.calendar.compose.basis.DefaultDayOfMonthContent
import epicarchitect.calendar.compose.basis.DefaultDayOfWeekContent
import epicarchitect.calendar.compose.basis.getByIndex
import epicarchitect.calendar.compose.basis.state.rememberBasisEpicCalendarState
import epicarchitect.calendar.compose.pager.config.LocalEpicCalendarPagerConfig
import epicarchitect.calendar.compose.pager.state.EpicCalendarPagerState
import epicarchitect.calendar.compose.pager.state.LocalEpicCalendarPagerState
import epicarchitect.calendar.compose.pager.state.rememberEpicCalendarPagerState
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EpicCalendarPager(
    modifier: Modifier = Modifier,
    pageModifier: (page: Int) -> Modifier = { Modifier },
    state: EpicCalendarPagerState = LocalEpicCalendarPagerState.current
        ?: rememberEpicCalendarPagerState(),
    onDayOfMonthClick: ((LocalDate) -> Unit)? = null,
    onDayOfWeekClick: ((DayOfWeek) -> Unit)? = null,
    dayOfWeekContent: BasisDayOfWeekContent = DefaultDayOfWeekContent,
    dayOfMonthContent: BasisDayOfMonthContent = DefaultDayOfMonthContent
) {
    CompositionLocalProvider(
        LocalEpicCalendarPagerConfig provides state.config,
        LocalEpicCalendarPagerState provides state
    ) {
        HorizontalPager(
            modifier = modifier,
            state = state.pagerState,
            verticalAlignment = Alignment.Top
        ) { page ->
            BasisEpicCalendar(
                modifier = pageModifier(page),
                state = rememberBasisEpicCalendarState(
                    currentMonth = remember(state.monthRange, page) {
                        state.monthRange.getByIndex(page)
                    },
                    config = state.config.basisConfig
                ),
                onDayOfMonthClick = onDayOfMonthClick,
                onDayOfWeekClick = onDayOfWeekClick,
                dayOfMonthContent = dayOfMonthContent,
                dayOfWeekContent = dayOfWeekContent
            )
        }
    }
}