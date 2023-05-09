import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import epicarchitect.calendar.compose.basis.BasisEpicCalendar
import epicarchitect.calendar.compose.basis.EpicMonth
import epicarchitect.calendar.compose.basis.atStartDay
import epicarchitect.calendar.compose.basis.config.rememberBasisEpicCalendarConfig
import epicarchitect.calendar.compose.basis.config.rememberMutableBasisEpicCalendarConfig
import epicarchitect.calendar.compose.basis.state.rememberMutableBasisEpicCalendarState
import epicarchitect.calendar.compose.ranges.drawEpicRanges
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Month
import kotlinx.datetime.plus

@Composable
fun BasisTesting() {
    val config = rememberMutableBasisEpicCalendarConfig()
    val state = rememberMutableBasisEpicCalendarState(
        currentMonth = EpicMonth(2000, Month.JANUARY),
        config = config
    )
    val rangeColor = MaterialTheme.colorScheme.primaryContainer
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

    BasisEpicCalendar(
        state = state,
        modifier = Modifier.drawEpicRanges(
            ranges = ranges,
            color = rangeColor
        )
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = state.currentMonth.year.toString()
        )

        Text(
            text = state.currentMonth.month.name
        )
    }

    Switch(
        onChanged = {
            config.displayDaysOfAdjacentMonths = it
        },
        checked = config.displayDaysOfAdjacentMonths,
        text = "displayDaysOfAdjacentMonths"
    )

    Switch(
        onChanged = {
            config.displayDaysOfWeek = it
        },
        checked = config.displayDaysOfWeek,
        text = "displayDaysOfWeek"
    )

}