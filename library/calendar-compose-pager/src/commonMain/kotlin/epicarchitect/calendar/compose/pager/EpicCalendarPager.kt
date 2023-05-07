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
import epicarchitect.calendar.compose.basis.BasisDayOfMonthContent
import epicarchitect.calendar.compose.basis.BasisDayOfWeekContent
import epicarchitect.calendar.compose.basis.BasisEpicCalendar
import epicarchitect.calendar.compose.basis.DefaultDayOfMonthContent
import epicarchitect.calendar.compose.basis.DefaultDayOfWeekContent
import epicarchitect.calendar.compose.basis.EpicMonth
import epicarchitect.calendar.compose.basis.addMonths
import epicarchitect.calendar.compose.basis.addYears
import epicarchitect.calendar.compose.basis.config.BasisEpicCalendarConfig
import epicarchitect.calendar.compose.basis.config.DefaultBasisEpicCalendarConfig
import epicarchitect.calendar.compose.basis.getByIndex
import epicarchitect.calendar.compose.basis.indexOf
import epicarchitect.calendar.compose.basis.size
import epicarchitect.calendar.compose.basis.state.rememberBasisEpicCalendarState
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EpicCalendarPager(
    modifier: Modifier = Modifier,
    pageModifier: (page: Int) -> Modifier = { Modifier },
    state: EpicCalendarPagerState = LocalEpicCalendarPagerState.current
        ?: rememberEpicCalendarPagerState(),
    config: EpicCalendarPagerConfig = LocalEpicCalendarPagerConfig.current,
    onDayOfMonthClick: ((LocalDate) -> Unit)? = null,
    onDayOfWeekClick: ((DayOfWeek) -> Unit)? = null,
    dayOfWeekContent: BasisDayOfWeekContent = DefaultDayOfWeekContent,
    dayOfMonthContent: BasisDayOfMonthContent = DefaultDayOfMonthContent
) = with(config) {
    CompositionLocalProvider(
        LocalEpicCalendarPagerConfig provides config,
        LocalEpicCalendarPagerState provides state
    ) {
        HorizontalPager(
            modifier = modifier,
            state = state.pagerState,
            pageCount = remember(state.monthRange) {
                state.monthRange.size()
            },
            verticalAlignment = Alignment.Top
        ) { page ->
            val basisState = rememberBasisEpicCalendarState(
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
                dayOfMonthContent = dayOfMonthContent,
                dayOfWeekContent = dayOfWeekContent
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberEpicCalendarPagerState(
    monthRange: ClosedRange<EpicMonth> = defaultMonthRange(),
    initialMonth: EpicMonth = monthRange.start,
    displayDaysOfAdjacentMonths: Boolean = true,
    displayDaysOfWeek: Boolean = true,
): EpicCalendarPagerState {
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

val DefaultEpicCalendarPagerConfig = ImmutableEpicCalendarPagerConfig(
    basisConfig = DefaultBasisEpicCalendarConfig
)

@Immutable
data class ImmutableEpicCalendarPagerConfig(
    override val basisConfig: BasisEpicCalendarConfig
) : EpicCalendarPagerConfig

interface EpicCalendarPagerConfig {
    val basisConfig: BasisEpicCalendarConfig
}

val LocalEpicCalendarPagerState = compositionLocalOf<EpicCalendarPagerState?> {
    null
}

val LocalEpicCalendarPagerConfig = compositionLocalOf<EpicCalendarPagerConfig> {
    DefaultEpicCalendarPagerConfig
}