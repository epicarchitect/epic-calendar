import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import epicarchitect.calendar.compose.basis.BasisEpicCalendar
import epicarchitect.calendar.compose.basis.atStartDay
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus

@Composable
fun BasisTesting() {
    val state = BasisEpicCalendar.rememberState()
    val testMonth = state.currentMonth
    val rangeColor = MaterialTheme.colorScheme.primaryContainer
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

    BasisEpicCalendar(
        state = state
    )

    Switch(
        onChanged = {
            state.displayDaysOfAdjacentMonths = it
        },
        checked = state.displayDaysOfAdjacentMonths,
        text = "displayDaysOfAdjacentMonths"
    )

    Switch(
        onChanged = {
            state.displayDaysOfWeek = it
        },
        checked = state.displayDaysOfWeek,
        text = "displayDaysOfWeek"
    )

}