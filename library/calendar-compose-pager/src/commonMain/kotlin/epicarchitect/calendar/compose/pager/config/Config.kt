package epicarchitect.calendar.compose.pager.config

import androidx.compose.runtime.compositionLocalOf
import epicarchitect.calendar.compose.basis.config.BasisEpicCalendarConfig

interface EpicCalendarPagerConfig {
    val basisConfig: BasisEpicCalendarConfig
}

val LocalEpicCalendarPagerConfig = compositionLocalOf<EpicCalendarPagerConfig> {
    DefaultEpicCalendarPagerConfig
}