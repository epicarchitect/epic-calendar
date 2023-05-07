import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import epicarchitect.calendar.compose.datepicker.EpicDatePicker
import epicarchitect.calendar.compose.datepicker.config.DefaultEpicDatePickerConfig
import epicarchitect.calendar.compose.datepicker.state.EpicDatePickerState
import epicarchitect.calendar.compose.datepicker.state.rememberEpicDatePickerState
import kotlinx.coroutines.launch

@Composable
fun DatePickerTesting() {
    val state = rememberEpicDatePickerState()
    val coroutineScope = rememberCoroutineScope()

    EpicDatePicker(
        state = state,
        config = DefaultEpicDatePickerConfig.copy(
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
            state.selectionMode = EpicDatePickerState.SelectionMode.Single(1)
        },
        checked = (state.selectionMode as? EpicDatePickerState.SelectionMode.Single)?.maxSize == 1,
        text = "Selection mode = Single(1)"
    )

    Switch(
        onChanged = {
            state.selectionMode = EpicDatePickerState.SelectionMode.Single(3)
        },
        checked = (state.selectionMode as? EpicDatePickerState.SelectionMode.Single)?.maxSize == 3,
        text = "Selection mode = Single(3)"
    )

    Switch(
        onChanged = {
            state.selectionMode = EpicDatePickerState.SelectionMode.Range
        },
        checked = state.selectionMode is EpicDatePickerState.SelectionMode.Range,
        text = "Selection mode = Range"
    )
}