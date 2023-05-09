package epicarchitect.sample.pagerTesting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import epicarchitect.sample.PrevNextButtons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import epicarchitect.calendar.compose.basis.addMonths
import epicarchitect.calendar.compose.pager.state.EpicCalendarPagerState
import epicarchitect.sample.Button
import epicarchitect.sample.TestingLayout
import epicarchitect.sample.TestingSection

@Composable
fun PagerStateControls(state: EpicCalendarPagerState) {
    TestingSection(
        title = "currentMonth: " + state.currentMonth.year.toString() + " " + state.currentMonth.month.name
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    state.scrollMonths(-1)
                },
                text = "-1 month"
            )

            Button(
                onClick = {
                    state.scrollMonths(1)
                },
                text = "+1 month"
            )

            Button(
                onClick = {
                    state.scrollYears(-1)
                },
                text = "-1 year"
            )

            Button(
                onClick = {
                    state.scrollYears(1)
                },
                text = "+1 year"
            )
        }
    }

}