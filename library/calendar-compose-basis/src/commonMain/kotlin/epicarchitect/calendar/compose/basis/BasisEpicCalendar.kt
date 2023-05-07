package epicarchitect.calendar.compose.basis

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

typealias BasisDayOfMonthContent = @Composable BoxScope.(LocalDate) -> Unit
typealias BasisDayOfWeekContent = @Composable BoxScope.(DayOfWeek) -> Unit

@Composable
fun BasisEpicCalendar(
    modifier: Modifier = Modifier,
    state: BasisEpicCalendarState = LocalBasisEpicCalendarState.current
        ?: rememberBasisEpicCalendarState(),
    config: BasisEpicCalendarConfig = LocalBasisEpicCalendarConfig.current,
    onDayOfMonthClick: ((LocalDate) -> Unit)? = null,
    onDayOfWeekClick: ((DayOfWeek) -> Unit)? = null,
    dayOfWeekContent: BasisDayOfWeekContent = DefaultDayOfWeekContent,
    dayOfMonthContent: BasisDayOfMonthContent = DefaultDayOfMonthContent
) = with(config) {
    CompositionLocalProvider(
        LocalBasisEpicCalendarConfig provides config,
        LocalBasisEpicCalendarState provides state
    ) {
        Column(
            modifier = modifier.then(
                Modifier.padding(contentPadding)
            ),
            verticalArrangement = Arrangement.spacedBy(rowsSpacerHeight),
        ) {
            if (state.displayDaysOfWeek) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    state.dateGridInfo.daysOfWeek.forEach { dayOfWeek ->
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(dayOfWeekViewShape)
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
                            if (state.displayDaysOfAdjacentMonths || date.epicMonth == state.currentMonth) {
                                Box(
                                    modifier = Modifier
                                        .clip(dayOfMonthViewShape)
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
fun rememberBasisEpicCalendarState(
    currentMonth: EpicMonth = EpicMonth.now(TimeZone.currentSystemDefault()),
    displayDaysOfWeek: Boolean = true,
    displayDaysOfAdjacentMonths: Boolean = true
): BasisEpicCalendarState {
    return remember(
        currentMonth,
        displayDaysOfWeek,
        displayDaysOfAdjacentMonths
    ) {
        DefaultBasisEpicCalendarState(
            currentMonth = currentMonth,
            displayDaysOfWeek = displayDaysOfWeek,
            displayDaysOfAdjacentMonths = displayDaysOfAdjacentMonths
        )
    }
}

class DefaultBasisEpicCalendarState(
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

val DefaultBasisEpicCalendarConfig = ImmutableBasisEpicCalendarConfig(
    rowsSpacerHeight = 4.dp,
    dayOfWeekViewHeight = 40.dp,
    dayOfMonthViewHeight = 40.dp,
    columnWidth = 40.dp,
    dayOfWeekViewShape = RoundedCornerShape(16.dp),
    dayOfMonthViewShape = RoundedCornerShape(16.dp),
    contentPadding = PaddingValues(0.dp),
    contentColor = Color.Unspecified
)

@Immutable
data class ImmutableBasisEpicCalendarConfig(
    override val rowsSpacerHeight: Dp,
    override val dayOfWeekViewHeight: Dp,
    override val dayOfMonthViewHeight: Dp,
    override val columnWidth: Dp,
    override val dayOfWeekViewShape: Shape,
    override val dayOfMonthViewShape: Shape,
    override val contentPadding: PaddingValues,
    override val contentColor: Color
) : BasisEpicCalendarConfig

interface BasisEpicCalendarState {
    val currentMonth: EpicMonth
    var displayDaysOfAdjacentMonths: Boolean
    var displayDaysOfWeek: Boolean
    val dateGridInfo: EpicCalendarGridInfo
}

interface BasisEpicCalendarConfig {
    val rowsSpacerHeight: Dp
    val dayOfWeekViewHeight: Dp
    val dayOfMonthViewHeight: Dp
    val columnWidth: Dp
    val dayOfWeekViewShape: Shape
    val dayOfMonthViewShape: Shape
    val contentPadding: PaddingValues
    val contentColor: Color
}

val LocalBasisEpicCalendarState = compositionLocalOf<BasisEpicCalendarState?> {
    null
}

val LocalBasisEpicCalendarConfig = compositionLocalOf<BasisEpicCalendarConfig> {
    DefaultBasisEpicCalendarConfig
}
