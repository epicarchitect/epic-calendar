import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus

@Composable
fun PagerTesting() {
    val state = EpicCalendarPager.rememberState()
    val testMonth = state.currentMonth
    val rangeColor = MaterialTheme.colorScheme.primaryContainer
    val coroutineScope = rememberCoroutineScope()
    val ranges = remember(testMonth, rangeColor) {
        listOf(
            testMonth
                .atStartDay()
                .let { it..it },
            testMonth
                .atStartDay()
                .let {
                    it.plus(1, DateTimeUnit.DAY)..it.plus(3, DateTimeUnit.DAY)
                }
        )
    }

    EpicCalendarPager(
        state = state
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