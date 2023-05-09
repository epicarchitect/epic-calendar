import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import epicarchitect.calendar.compose.basis.config.rememberMutableBasisEpicCalendarConfig
import epicarchitect.calendar.compose.datepicker.EpicDatePicker
import epicarchitect.calendar.compose.datepicker.config.DefaultEpicDatePickerConfig
import epicarchitect.calendar.compose.datepicker.config.rememberEpicDatePickerConfig
import epicarchitect.calendar.compose.datepicker.state.EpicDatePickerState
import epicarchitect.calendar.compose.datepicker.state.rememberEpicDatePickerState
import epicarchitect.calendar.compose.pager.config.rememberEpicCalendarPagerConfig
import kotlinx.coroutines.launch

@Composable
fun DatePickerTesting() {
    val basisConfig = rememberMutableBasisEpicCalendarConfig()
    val state = rememberEpicDatePickerState(
        config = rememberEpicDatePickerConfig(
            pagerConfig = rememberEpicCalendarPagerConfig(
                basisConfig = basisConfig
            ),
            selectionContentColor = MaterialTheme.colorScheme.onPrimary,
            selectionContainerColor = MaterialTheme.colorScheme.primary
        )
    )
    val coroutineScope = rememberCoroutineScope()

    EpicDatePicker(
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
        onChanged = { basisConfig.displayDaysOfAdjacentMonths = it },
        checked = basisConfig.displayDaysOfAdjacentMonths,
        text = "displayDaysOfAdjacentMonths"
    )

    Switch(
        onChanged = { basisConfig.displayDaysOfWeek = it },
        checked = basisConfig.displayDaysOfWeek,
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