import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import epicarchitect.calendar.compose.basis.atStartDay
import epicarchitect.calendar.compose.pager.EpicCalendarPager
import epicarchitect.calendar.compose.pager.rememberEpicCalendarPagerState
import epicarchitect.calendar.compose.ranges.drawEpicRanges
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus

@Composable
fun PagerTesting() {
    val state = rememberEpicCalendarPagerState()
    val rangeColor = MaterialTheme.colorScheme.primaryContainer
    val coroutineScope = rememberCoroutineScope()
    val ranges = remember {
        val month = state.currentMonth
        listOf(
            month.atStartDay().let { it..it },
            month.atStartDay().let {
                it.plus(1, DateTimeUnit.DAY)..it.plus(3, DateTimeUnit.DAY)
            },
            month.atStartDay().let {
                it.plus(13, DateTimeUnit.DAY)..it.plus(55, DateTimeUnit.DAY)
            }
        )
    }

    EpicCalendarPager(
        state = state,
        pageModifier = {
            Modifier.drawEpicRanges(
                ranges = ranges,
                color = rangeColor
            )
        }
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            text = "prev",
            onClick = {
                coroutineScope.launch {
                    state.scrollMonths(-1)
                }
            }
        )

        Button(
            text = "next",
            onClick = {
                coroutineScope.launch {
                    state.scrollMonths(1)
                }
            }
        )

        Text(
            text = state.currentMonth.month.name
        )
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            text = "prev",
            onClick = {
                coroutineScope.launch {
                    state.scrollYears(-1)
                }
            }
        )

        Button(
            text = "next",
            onClick = {
                coroutineScope.launch {
                    state.scrollYears(1)
                }
            }
        )

        Text(
            text = state.currentMonth.year.toString()
        )
    }

    Switch(
        onChanged = { state.displayDaysOfAdjacentMonths = it },
        checked = state.displayDaysOfAdjacentMonths,
        text = "displayDaysOfAdjacentMonths"
    )

    Switch(
        onChanged = { state.displayDaysOfWeek = it },
        checked = state.displayDaysOfWeek,
        text = "displayDaysOfWeek"
    )
}