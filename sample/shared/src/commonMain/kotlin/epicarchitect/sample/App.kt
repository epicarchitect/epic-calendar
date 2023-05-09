package epicarchitect.sample

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import epicarchitect.calendar.compose.basis.config.DefaultBasisEpicCalendarConfig
import epicarchitect.calendar.compose.basis.config.LocalBasisEpicCalendarConfig
import epicarchitect.sample.basisTesting.BasisTesting
import epicarchitect.sample.datepickerTesting.DatePickerTesting
import epicarchitect.sample.pagerTesting.PagerTesting

private val testingPages = mapOf<String, @Composable () -> Unit>(
    "Basis" to { BasisTesting() },
    "Pager" to { PagerTesting() },
    "DatePicker" to { DatePickerTesting() }
)

@Composable
fun App() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                var currentPage by remember {
                    mutableStateOf(0)
                }

                TabRow(
                    modifier = Modifier.fillMaxWidth(),
                    selectedTabIndex = currentPage,
                ) {
                    testingPages.keys.forEachIndexed { index, title ->
                        Box(
                            modifier = Modifier
                                .height(40.dp)
                                .clickable {
                                    currentPage = index
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                text = title,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                CompositionLocalProvider(
                    LocalBasisEpicCalendarConfig provides DefaultBasisEpicCalendarConfig.copy(
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    )
                ) {
                    testingPages[testingPages.keys.toList()[currentPage]]!!()
                }
            }
        }
    }
}