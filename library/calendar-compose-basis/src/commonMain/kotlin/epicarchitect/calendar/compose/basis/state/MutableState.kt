package epicarchitect.calendar.compose.basis.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import epicarchitect.calendar.compose.basis.EpicMonth
import epicarchitect.calendar.compose.basis.config.BasisEpicCalendarConfig
import epicarchitect.calendar.compose.basis.config.LocalBasisEpicCalendarConfig

@Stable
class MutableBasisEpicCalendarState(
    config: BasisEpicCalendarConfig,
    currentMonth: EpicMonth
) : BasisEpicCalendarState {
    override var config by mutableStateOf(config)
    override var currentMonth by mutableStateOf(currentMonth)

    override val dateGridInfo by derivedStateOf {
        calculateEpicCalendarGridInfo(
            currentMonth = this.currentMonth,
            config = config
        )
    }
}

@Stable
@Composable
fun rememberMutableBasisEpicCalendarState(
    config: BasisEpicCalendarConfig = LocalBasisEpicCalendarConfig.current,
    currentMonth: EpicMonth = LocalBasisEpicCalendarState.current?.currentMonth ?: EpicMonth.now(),
): MutableBasisEpicCalendarState = remember(
    currentMonth,
    config
) {
    MutableBasisEpicCalendarState(
        currentMonth = currentMonth,
        config = config
    )
}