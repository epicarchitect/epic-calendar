import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import epicarchitect.calendar.compose.datePicker.EpicDatePicker
import kotlinx.coroutines.launch

@Composable
fun DatePickerTesting() {
    val state = EpicDatePicker.rememberState()
    val coroutineScope = rememberCoroutineScope()

    EpicDatePicker(
        state = state,
        config = EpicDatePicker.DefaultConfig.copy(
            selectionContentColor = MaterialTheme.colorScheme.onPrimary,
            selectionContainerColor = MaterialTheme.colorScheme.primary
        )
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            text = "prev",
            onClick = {
                coroutineScope.launch {
                    state.pagerState.scrollMonths(-1)
                }
            }
        )

        Button(
            text = "next",
            onClick = {
                coroutineScope.launch {
                    state.pagerState.scrollMonths(1)
                }
            }
        )

        Text(
            text = state.pagerState.currentMonth.month.name
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
                    state.pagerState.scrollYears(-1)
                }
            }
        )

        Button(
            text = "next",
            onClick = {
                coroutineScope.launch {
                    state.pagerState.scrollYears(1)
                }
            }
        )

        Text(
            text = state.pagerState.currentMonth.year.toString()
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

    Switch(
        onChanged = {
            state.selectionMode = EpicDatePicker.SelectionMode.Single(1)
        },
        checked = (state.selectionMode as? EpicDatePicker.SelectionMode.Single)?.maxSize == 1,
        text = "Selection mode = Single(1)"
    )

    Switch(
        onChanged = {
            state.selectionMode = EpicDatePicker.SelectionMode.Single(3)
        },
        checked = (state.selectionMode as? EpicDatePicker.SelectionMode.Single)?.maxSize == 3,
        text = "Selection mode = Single(3)"
    )

    Switch(
        onChanged = {
            state.selectionMode = EpicDatePicker.SelectionMode.Range
        },
        checked = state.selectionMode is EpicDatePicker.SelectionMode.Range,
        text = "Selection mode = Range"
    )
}