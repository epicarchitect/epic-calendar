import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import epicarchitect.calendar.compose.basis.BasisEpicCalendar
import epicarchitect.calendar.compose.basis.atStartDay
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus

expect fun getPlatformName(): String

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
                Text(getPlatformName())

                BasisTesting()
            }
        }
    }

}

@Composable
fun BasisTesting() {
    val basisState = BasisEpicCalendar.rememberState()
    val testMonth = basisState.currentMonth
    val rangeColor = MaterialTheme.colorScheme.primaryContainer
    val ranges = remember(testMonth, rangeColor) {
        listOf(
            testMonth
                .atStartDay()
                .let { it..it },
            testMonth
                .atStartDay()
                .let {
                    it.plus(1, DateTimeUnit.DAY)..it.plus(3, DateTimeUnit.DAY)
                }
        )
    }

    BasisEpicCalendar(
        state = basisState
    )
}