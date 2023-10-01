package epicarchitect.sample.datepickerTesting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import epicarchitect.calendar.compose.datepicker.state.EpicDatePickerState
import epicarchitect.sample.TestingSection

private val testModes = listOf(
    EpicDatePickerState.SelectionMode.Single(1),
    EpicDatePickerState.SelectionMode.Single(3),
    EpicDatePickerState.SelectionMode.Range
)

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DatePickerStateControls(state: EpicDatePickerState) {
    TestingSection(
        "selectionMode: ${
            when (val mode = state.selectionMode) {
                EpicDatePickerState.SelectionMode.Range -> "Range"
                is EpicDatePickerState.SelectionMode.Single -> "Single(${mode.maxSize})"
            }
        }"
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            testModes.forEach {
                Card(
                    onClick = {
                        state.selectionMode = it
                    },
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (state.selectionMode == it) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            Color.Unspecified
                        }
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = when (val mode = it) {
                            EpicDatePickerState.SelectionMode.Range -> "Range"
                            is EpicDatePickerState.SelectionMode.Single -> "Single(${mode.maxSize})"
                        }
                    )
                }
            }
        }
    }
}