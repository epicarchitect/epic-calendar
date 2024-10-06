package epicarchitect.calendar.compose.pager.config

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import epicarchitect.calendar.compose.basis.config.BasisEpicCalendarConfig

class MutableEpicCalendarPagerConfig(
    basisConfig: BasisEpicCalendarConfig
) : EpicCalendarPagerConfig {
    override var basisConfig by mutableStateOf(basisConfig)
}

@Composable
fun rememberMutableEpicCalendarPagerConfig(
    basisConfig: BasisEpicCalendarConfig = LocalEpicCalendarPagerConfig.current.basisConfig
): MutableEpicCalendarPagerConfig = remember(basisConfig) {
    MutableEpicCalendarPagerConfig(basisConfig)
}