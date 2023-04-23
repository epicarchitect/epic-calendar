package epicarchitect.calendar.compose.ranges

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.inset
import epicarchitect.calendar.compose.basis.BasisEpicCalendar
import kotlinx.datetime.LocalDate

fun Modifier.drawEpicRanges(
    basisState: BasisEpicCalendar.State,
    basisConfig: BasisEpicCalendar.Config,
    ranges: List<ClosedRange<LocalDate>>,
    color: Color
) = composed {
    val gridInfo = basisState.dateGridInfo
    val displayDaysOfAdjacentMonths = basisState.displayDaysOfAdjacentMonths

    val rangeInfoList = remember(ranges, displayDaysOfAdjacentMonths, gridInfo) {
        ranges.mapNotNull {
            calculateEpicRangeInfo(
                displayDaysOfAdjacentMonths = displayDaysOfAdjacentMonths,
                gridInfo = gridInfo,
                startDate = it.start,
                endDate = it.endInclusive,
            )
        }
    }

    drawBehind {
        val contentPadding = basisConfig.contentPadding
        val contentPaddingTop = contentPadding.calculateTopPadding()
        val contentPaddingLeft = contentPadding.calculateLeftPadding(layoutDirection)
        val contentPaddingRight = contentPadding.calculateRightPadding(layoutDirection)
        val contentPaddingBottom = contentPadding.calculateBottomPadding()
        val dayOfWeekHeightPx = basisConfig.dayOfWeekViewHeight.toPx()
        val dayOfMonthViewHeightPx = basisConfig.dayOfMonthViewHeight.toPx()
        val rowsSpacerHeightPx = basisConfig.rowsSpacerHeight.toPx()
        val columnWidthPx = basisConfig.columnWidth.toPx()
        val itemSize = Size(columnWidthPx, dayOfMonthViewHeightPx)

        inset(
            top = contentPaddingTop.toPx().let {
                if (basisState.displayDaysOfWeek.not()) it
                else it + dayOfWeekHeightPx + rowsSpacerHeightPx
            },
            bottom = contentPaddingBottom.toPx(),
            left = contentPaddingLeft.toPx(),
            right = contentPaddingRight.toPx()
        ) {
            rangeInfoList.forEach { info ->
                drawEpicRange(
                    rangeInfo = info,
                    color = color,
                    itemContainerWidthPx = size.width / 7f,
                    itemSize = itemSize,
                    rowsSpacerHeightPx = rowsSpacerHeightPx,
                    dayOfMonthShape = basisConfig.dayOfMonthViewShape
                )
            }
        }
    }
}