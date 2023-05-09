package epicarchitect.sample.basisTesting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import epicarchitect.calendar.compose.basis.addMonths
import epicarchitect.calendar.compose.basis.addYears
import epicarchitect.calendar.compose.basis.config.DefaultBasisEpicCalendarConfig
import epicarchitect.calendar.compose.basis.config.MutableBasisEpicCalendarConfig
import epicarchitect.calendar.compose.basis.state.MutableBasisEpicCalendarState
import epicarchitect.sample.Button
import epicarchitect.sample.Switch
import epicarchitect.sample.TestingSection

private val testShapes = listOf(
    DefaultBasisEpicCalendarConfig.dayOfMonthShape,
    CircleShape,
    CutCornerShape(8.dp)
)

@Composable
fun BasisConfigControls(config: MutableBasisEpicCalendarConfig) {

    TestingSection(
        title = "displayDaysOfAdjacentMonths: ${config.displayDaysOfAdjacentMonths}"
    ) {
        Switch(
            onChanged = {
                config.displayDaysOfAdjacentMonths = it
            },
            checked = config.displayDaysOfAdjacentMonths,
            text = "displayDaysOfAdjacentMonths"
        )
    }

    TestingSection(
        title = "displayDaysOfWeek: ${config.displayDaysOfWeek}"
    ) {
        Switch(
            onChanged = {
                config.displayDaysOfWeek = it
            },
            checked = config.displayDaysOfWeek,
            text = "displayDaysOfWeek"
        )
    }

    TestingSection(
        title = "dayOfMonthShape: ${
            when (config.dayOfMonthShape) {
                testShapes[0] -> "RoundedCorner(16.dp)"
                testShapes[1] -> "Circle"
                else -> "CutCorner(8.dp)"
            }
        }"
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            testShapes.forEach { shape ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(shape)
                        .background(
                            if (config.dayOfMonthShape == shape) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                            }
                        ).clickable {
                            config.dayOfMonthShape = shape
                        }
                )
            }
        }
    }

    TestingSection(
        title = "rowsSpacerHeight: ${config.rowsSpacerHeight}"
    ) {
        Slider(
            value = config.rowsSpacerHeight.value,
            onValueChange = { config.rowsSpacerHeight = it.dp },
            valueRange = 0f..100f
        )
    }

    TestingSection(
        title = "dayOfMonthViewHeight: ${config.dayOfMonthViewHeight}"
    ) {
        Slider(
            value = config.dayOfMonthViewHeight.value,
            onValueChange = { config.dayOfMonthViewHeight = it.dp },
            valueRange = 0f..100f
        )
    }

    TestingSection(
        title = "dayOfWeekViewHeight: ${config.dayOfWeekViewHeight}"
    ) {
        Slider(
            value = config.dayOfWeekViewHeight.value,
            onValueChange = { config.dayOfWeekViewHeight = it.dp },
            valueRange = 0f..100f
        )
    }
}

@Composable
fun BasisStateControls(state: MutableBasisEpicCalendarState) {
    TestingSection(
        "currentMonth: " + state.currentMonth.year.toString() + " " + state.currentMonth.month.name
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    state.currentMonth = state.currentMonth.addMonths(-1)
                },
                text = "-1 month"
            )

            Button(
                onClick = {
                    state.currentMonth = state.currentMonth.addMonths(1)
                },
                text = "+1 month"
            )

            Button(
                onClick = {
                    state.currentMonth = state.currentMonth.addYears(-1)
                },
                text = "-1 year"
            )

            Button(
                onClick = {
                    state.currentMonth = state.currentMonth.addYears(1)
                },
                text = "+1 year"
            )
        }
    }
}
