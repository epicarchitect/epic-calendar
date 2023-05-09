package epicarchitect.sample.basisTesting

import epicarchitect.sample.PrevNextButtons
import epicarchitect.sample.Switch
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import epicarchitect.calendar.compose.basis.addYears
import epicarchitect.calendar.compose.basis.config.MutableBasisEpicCalendarConfig
import epicarchitect.calendar.compose.basis.next
import epicarchitect.calendar.compose.basis.previous
import epicarchitect.calendar.compose.basis.state.MutableBasisEpicCalendarState

@Composable
fun BasisConfigControls(config: MutableBasisEpicCalendarConfig) {
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

    Text("rowsSpacerHeight: ${config.rowsSpacerHeight}")
    Slider(
        value = config.rowsSpacerHeight.value,
        onValueChange = { config.rowsSpacerHeight = it.dp },
        valueRange = 0f..100f
    )

    Text("dayOfMonthViewHeight: ${config.dayOfMonthViewHeight}")
    Slider(
        value = config.dayOfMonthViewHeight.value,
        onValueChange = { config.dayOfMonthViewHeight = it.dp },
        valueRange = 0f..100f
    )

    Text("dayOfWeekViewHeight: ${config.dayOfWeekViewHeight}")
    Slider(
        value = config.dayOfWeekViewHeight.value,
        onValueChange = { config.dayOfWeekViewHeight = it.dp },
        valueRange = 0f..100f
    )
}

@Composable
fun BasisStateControls(state: MutableBasisEpicCalendarState) {
    PrevNextButtons(
        onPrev = { state.currentMonth = state.currentMonth.previous() },
        onNext = { state.currentMonth = state.currentMonth.next() },
        text = state.currentMonth.month.name
    )

    PrevNextButtons(
        onPrev = { state.currentMonth = state.currentMonth.addYears(-1) },
        onNext = { state.currentMonth = state.currentMonth.addYears(1) },
        text = state.currentMonth.year.toString()
    )
}
