package epicarchitect.calendar.compose.pager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import epicarchitect.calendar.compose.basis.EpicDayOfWeek
import epicarchitect.calendar.compose.basis.EpicMonth
import epicarchitect.calendar.compose.basis.addMonths
import epicarchitect.calendar.compose.basis.addYears
import epicarchitect.calendar.compose.basis.BasisDayOfMonthComposable
import epicarchitect.calendar.compose.basis.BasisDayOfWeekComposable
import epicarchitect.calendar.compose.basis.BasisEpicCalendar
import epicarchitect.calendar.compose.basis.getByIndex
import epicarchitect.calendar.compose.basis.indexOf
import epicarchitect.calendar.compose.basis.size
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import java.time.Month


object EpicCalendarPager {
    private fun defaultMonthRange() = EpicMonth(2000, Month.JANUARY)..EpicMonth.now(
        TimeZone.currentSystemDefault()
    )

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun rememberState(
        monthRange: ClosedRange<EpicMonth> = defaultMonthRange(),
        initialMonth: EpicMonth = monthRange.start,
        displayDaysOfAdjacentMonths: Boolean = true,
        displayDaysOfWeek: Boolean = true,
    ): State {
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
            DefaultState(
                pagerState = pagerState,
                displayDaysOfAdjacentMonths = displayDaysOfAdjacentMonths,
                displayDaysOfWeek = displayDaysOfWeek,
                monthRange = monthRange
            )
        }
    }


    @OptIn(ExperimentalFoundationApi::class)
    class DefaultState(
        monthRange: ClosedRange<EpicMonth>,
        displayDaysOfAdjacentMonths: Boolean,
        displayDaysOfWeek: Boolean,
        override val pagerState: PagerState
    ) : State {
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
    interface State {
        val currentMonth: EpicMonth
        var monthRange: ClosedRange<EpicMonth>
        var displayDaysOfAdjacentMonths: Boolean
        var displayDaysOfWeek: Boolean
        val pagerState: PagerState
        suspend fun scrollToMonth(month: EpicMonth)
        suspend fun scrollYears(amount: Int)
        suspend fun scrollMonths(amount: Int)
    }

    val DefaultConfig = ImmutableConfig(
        basisConfig = BasisEpicCalendar.DefaultConfig
    )

    @Immutable
    data class ImmutableConfig(
        override val basisConfig: BasisEpicCalendar.Config
    ) : Config

    interface Config {
        val basisConfig: BasisEpicCalendar.Config
    }

    val LocalState = compositionLocalOf<State?> {
        null
    }

    val LocalConfig = compositionLocalOf<Config> {
        DefaultConfig
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EpicCalendarPager(
    modifier: Modifier = Modifier,
    pageModifier: (page: Int) -> Modifier = { Modifier },
    state: EpicCalendarPager.State = EpicCalendarPager.LocalState.current
        ?: EpicCalendarPager.rememberState(),
    config: EpicCalendarPager.Config = EpicCalendarPager.LocalConfig.current,
    onDayOfMonthClick: ((LocalDate) -> Unit)? = null,
    onDayOfWeekClick: ((EpicDayOfWeek) -> Unit)? = null,
    dayOfWeekComposable: BasisDayOfWeekComposable = BasisEpicCalendar.DefaultDayOfWeekComposable,
    dayOfMonthComposable: BasisDayOfMonthComposable = BasisEpicCalendar.DefaultDayOfMonthComposable
) = with(config) {
    CompositionLocalProvider(
        EpicCalendarPager.LocalConfig provides config,
        EpicCalendarPager.LocalState provides state
    ) {
        HorizontalPager(
            modifier = modifier,
            state = state.pagerState,
            pageCount = remember(state.monthRange) {
                state.monthRange.size()
            },
            verticalAlignment = Alignment.Top
        ) { page ->
            val basisState = BasisEpicCalendar.rememberState(
                currentMonth = remember(state.monthRange, page) {
                    state.monthRange.getByIndex(page)
                },
                displayDaysOfAdjacentMonths = state.displayDaysOfAdjacentMonths,
                displayDaysOfWeek = state.displayDaysOfWeek
            )

            BasisEpicCalendar(
                modifier = pageModifier(page),
                state = basisState,
                config = basisConfig,
                onDayOfMonthClick = onDayOfMonthClick,
                onDayOfWeekClick = onDayOfWeekClick,
                dayOfMonthComposable = dayOfMonthComposable,
                dayOfWeekComposable = dayOfWeekComposable
            )
        }
    }
}
