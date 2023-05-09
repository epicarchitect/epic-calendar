package epicarchitect.calendar.compose.basis.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import epicarchitect.calendar.compose.basis.EpicMonth
import epicarchitect.calendar.compose.basis.config.BasisEpicCalendarConfig
import epicarchitect.calendar.compose.basis.config.LocalBasisEpicCalendarConfig

@Stable
class ImmutableBasisEpicCalendarState(
    override val currentMonth: EpicMonth,
    override val config: BasisEpicCalendarConfig
) : BasisEpicCalendarState {
    override val dateGridInfo by derivedStateOf {
        calculateEpicCalendarGridInfo(
            currentMonth = currentMonth,
            config = config
        )
    }
}

@Stable
@Composable
fun rememberBasisEpicCalendarState(
    currentMonth: EpicMonth = LocalBasisEpicCalendarState.current?.currentMonth ?: EpicMonth.now(),
    config: BasisEpicCalendarConfig = LocalBasisEpicCalendarConfig.current
): BasisEpicCalendarState = remember(
    currentMonth,
    config
) {
    ImmutableBasisEpicCalendarState(
        currentMonth = currentMonth,
        config = config
    )
}