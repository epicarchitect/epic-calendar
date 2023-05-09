import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import epicarchitect.calendar.compose.basis.config.DefaultBasisEpicCalendarConfig
import epicarchitect.calendar.compose.basis.config.LocalBasisEpicCalendarConfig


@Composable
fun App() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                var currentPage by remember {
                    mutableStateOf(0)
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { currentPage -= 1 },
                        text = "prev",
                        enabled = currentPage > 0
                    )
                    Button(
                        onClick = { currentPage += 1 },
                        text = "next",
                        enabled = currentPage < 2
                    )
                    Text(
                        text = when (currentPage) {
                            0 -> "${getPlatformName()} Basis"
                            1 -> "${getPlatformName()} Pager"
                            2 -> "${getPlatformName()} DatePicker"
                            else -> "Empty"
                        }
                    )
                }

                CompositionLocalProvider(
                    LocalBasisEpicCalendarConfig provides DefaultBasisEpicCalendarConfig.copy(
                        dayOfMonthViewShape = CutCornerShape(20),
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