package epicarchitect.sample.pagerTesting

import epicarchitect.sample.TestingLayout
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import epicarchitect.sample.basisTesting.BasisConfigControls
import epicarchitect.calendar.compose.basis.config.rememberMutableBasisEpicCalendarConfig
import epicarchitect.calendar.compose.pager.EpicCalendarPager
import epicarchitect.calendar.compose.pager.config.rememberEpicCalendarPagerConfig
import epicarchitect.calendar.compose.pager.state.rememberEpicCalendarPagerState
import epicarchitect.calendar.compose.ranges.drawEpicRanges
import epicarchitect.sample.TestingSection
import epicarchitect.sample.rangesTesting.testRanges

@Composable
fun PagerTesting() {
    val basisConfig = rememberMutableBasisEpicCalendarConfig()
    val state = rememberEpicCalendarPagerState(
        config = rememberEpicCalendarPagerConfig(
            basisConfig = basisConfig
        )
    )
    val rangeColor = MaterialTheme.colorScheme.primaryContainer

    TestingLayout(
        controls = {
            PagerStateControls(state)
            TestingSection(title = "BasisConfig") {
                BasisConfigControls(basisConfig)
            }
        }
    ) {
        EpicCalendarPager(
            state = state,
            pageModifier = {
                Modifier.drawEpicRanges(
                    ranges = testRanges,
                    color = rangeColor
                )
            }
        )
    }
}