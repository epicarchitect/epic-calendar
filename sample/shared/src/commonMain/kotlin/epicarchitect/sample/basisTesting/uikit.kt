package epicarchitect.sample.basisTesting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import epicarchitect.calendar.compose.basis.addYears
import epicarchitect.calendar.compose.basis.config.DefaultBasisEpicCalendarConfig
import epicarchitect.calendar.compose.basis.config.MutableBasisEpicCalendarConfig
import epicarchitect.calendar.compose.basis.next
import epicarchitect.calendar.compose.basis.previous
import epicarchitect.calendar.compose.basis.state.MutableBasisEpicCalendarState
import epicarchitect.sample.PrevNextButtons
import epicarchitect.sample.Switch

private val testShapes = listOf(
    DefaultBasisEpicCalendarConfig.dayOfMonthViewShape,
    RoundedCornerShape(100),
    CutCornerShape(16.dp)
)

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


    Text("DayOfMonthShape")
    Row(
        modifier = Modifier.padding(top = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        testShapes.forEach { shape ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(shape)
                    .background(
                        if (config.dayOfMonthViewShape == shape) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                        }
                    ).clickable {
                        config.dayOfMonthViewShape = shape
                    }
            )
        }
    }
    Slider(
        value = config.rowsSpacerHeight.value,
        onValueChange = { config.rowsSpacerHeight = it.dp },
        valueRange = 0f..100f
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
