package epicarchitect.sample.datepickerTesting

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import epicarchitect.calendar.compose.basis.config.rememberMutableBasisEpicCalendarConfig
import epicarchitect.calendar.compose.datepicker.EpicDatePicker
import epicarchitect.calendar.compose.datepicker.config.rememberEpicDatePickerConfig
import epicarchitect.calendar.compose.datepicker.state.rememberEpicDatePickerState
import epicarchitect.calendar.compose.pager.config.rememberEpicCalendarPagerConfig
import epicarchitect.sample.TestingLayout
import epicarchitect.sample.TestingSection
import epicarchitect.sample.basisTesting.BasisConfigControls
import epicarchitect.sample.pagerTesting.PagerStateControls

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

    TestingLayout(
        controls = {
            PagerStateControls(state.pagerState)
            DatePickerStateControls(state)
            TestingSection(title = "BasisConfig") {
                BasisConfigControls(basisConfig)
            }
        }
    ) {
        EpicDatePicker(state = state)
    }
}