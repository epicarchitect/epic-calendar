package epicarchitect.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import epicarchitect.calendar.compose.basis.config.DefaultBasisEpicCalendarConfig
import epicarchitect.calendar.compose.basis.config.LocalBasisEpicCalendarConfig
import epicarchitect.sample.basisTesting.BasisTesting
import epicarchitect.sample.datepickerTesting.DatePickerTesting
import epicarchitect.sample.pagerTesting.PagerTesting

@Composable
fun App() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                var currentPage by remember {
                    mutableStateOf(0)
                }

                PrevNextButtons(
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                    onPrev = { currentPage -= 1 },
                    onNext = { currentPage += 1 },
                    text = when (currentPage) {
                        0 -> "${getPlatformName()} Basis"
                        1 -> "${getPlatformName()} Pager"
                        2 -> "${getPlatformName()} DatePicker"
                        else -> "Empty"
                    }
                )

                CompositionLocalProvider(
                    LocalBasisEpicCalendarConfig provides DefaultBasisEpicCalendarConfig.copy(
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    )
                ) {
                    when (currentPage) {
                        0 -> BasisTesting()
                        1 -> PagerTesting()
                        2 -> DatePickerTesting()
                    }
                }
            }
        }
    }
}