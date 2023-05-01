import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


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
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { currentPage -= 1 },
                        text = "prev"
                    )
                    Text(
                        text = when (currentPage) {
                            0 -> "${getPlatformName()} Basis"
                            1 -> "${getPlatformName()} Pager"
                            2 -> "${getPlatformName()} DatePicker"
                            else -> "Empty"
                        }
                    )
                    Button(
                        onClick = { currentPage += 1 },
                        text = "next"
                    )
                }

                when (currentPage) {
                    0 -> BasisTesting()
                    1 -> PagerTesting()
                    2 -> DatePickerTesting()
                }
            }
        }
    }
}