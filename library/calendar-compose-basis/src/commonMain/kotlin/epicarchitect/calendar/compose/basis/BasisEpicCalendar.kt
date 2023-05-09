package epicarchitect.calendar.compose.basis

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import epicarchitect.calendar.compose.basis.config.LocalBasisEpicCalendarConfig
import epicarchitect.calendar.compose.basis.state.BasisEpicCalendarState
import epicarchitect.calendar.compose.basis.state.LocalBasisEpicCalendarState
import epicarchitect.calendar.compose.basis.state.rememberBasisEpicCalendarState
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

typealias BasisDayOfMonthContent = @Composable BoxScope.(LocalDate) -> Unit
typealias BasisDayOfWeekContent = @Composable BoxScope.(DayOfWeek) -> Unit

val DefaultDayOfMonthContent: BasisDayOfMonthContent = { date ->
    val state = LocalBasisEpicCalendarState.current!!
    val config = LocalBasisEpicCalendarConfig.current
    Text(
        modifier = Modifier.alpha(
            alpha = remember(date, state.currentMonth) {
                if (date in state.currentMonth) 1.0f else 0.5f
            }
        ),
        text = date.dayOfMonth.toString(),
        textAlign = TextAlign.Center,
        color = config.contentColor
    )
}

val DefaultDayOfWeekContent: BasisDayOfWeekContent = { dayOfWeek ->
    val config = LocalBasisEpicCalendarConfig.current
    Text(
        text = dayOfWeek.localized(),
        textAlign = TextAlign.Center,
        color = config.contentColor
    )
}

@Composable
fun BasisEpicCalendar(
    modifier: Modifier = Modifier,
    state: BasisEpicCalendarState = LocalBasisEpicCalendarState.current
        ?: rememberBasisEpicCalendarState(),
    onDayOfMonthClick: ((LocalDate) -> Unit)? = null,
    onDayOfWeekClick: ((DayOfWeek) -> Unit)? = null,
    dayOfWeekContent: BasisDayOfWeekContent = DefaultDayOfWeekContent,
    dayOfMonthContent: BasisDayOfMonthContent = DefaultDayOfMonthContent
) = with(state.config) {
    CompositionLocalProvider(
        LocalBasisEpicCalendarConfig provides state.config,
        LocalBasisEpicCalendarState provides state
    ) {
        Column(
            modifier = modifier.then(
                Modifier.padding(contentPadding)
            ),
            verticalArrangement = Arrangement.spacedBy(rowsSpacerHeight),
        ) {
            if (displayDaysOfWeek) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    daysOfWeek.forEach { dayOfWeek ->
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(dayOfWeekShape)
                                    .height(dayOfWeekViewHeight)
                                    .width(columnWidth)
                                    .let {
                                        if (onDayOfWeekClick == null) it
                                        else it.clickable {
                                            onDayOfWeekClick(dayOfWeek)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                dayOfWeekContent(dayOfWeek)
                            }
                        }
                    }
                }
            }

            state.dateGridInfo.dateMatrix.forEach { rowDates ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rowDates.forEach { date ->
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            if (displayDaysOfAdjacentMonths || date.epicMonth == state.currentMonth) {
                                Box(
                                    modifier = Modifier
                                        .clip(dayOfMonthShape)
                                        .height(dayOfMonthViewHeight)
                                        .width(columnWidth)
                                        .let {
                                            if (onDayOfMonthClick == null) it
                                            else it.clickable {
                                                onDayOfMonthClick(date)
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    dayOfMonthContent(date)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}