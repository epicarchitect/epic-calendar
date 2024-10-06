package epicarchitect.calendar.compose.pager.config

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import epicarchitect.calendar.compose.basis.config.BasisEpicCalendarConfig

@Immutable
data class ImmutableEpicCalendarPagerConfig(
    override val basisConfig: BasisEpicCalendarConfig
) : EpicCalendarPagerConfig

@Composable
fun rememberEpicCalendarPagerConfig(
    basisConfig: BasisEpicCalendarConfig = LocalEpicCalendarPagerConfig.current.basisConfig
): EpicCalendarPagerConfig = remember(basisConfig) {
    ImmutableEpicCalendarPagerConfig(basisConfig)
}