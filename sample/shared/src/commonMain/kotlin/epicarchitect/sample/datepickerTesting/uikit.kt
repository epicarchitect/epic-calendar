package epicarchitect.sample.datepickerTesting

import epicarchitect.sample.Switch
import androidx.compose.runtime.Composable
import epicarchitect.calendar.compose.datepicker.state.EpicDatePickerState

@Composable
fun DatePickerStateControls(state: EpicDatePickerState) {
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