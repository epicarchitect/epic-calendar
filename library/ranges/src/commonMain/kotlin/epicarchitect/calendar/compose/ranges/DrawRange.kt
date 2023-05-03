package epicarchitect.calendar.compose.ranges

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.Density

internal fun DrawScope.drawEpicRange(
    rangeInfo: EpicRangeInfo,
    color: Color,
    itemSize: Size,
    itemContainerWidthPx: Float,
    rowsSpacerHeightPx: Float,
    dayOfMonthShape: Shape
) {
    val (x1, y1) = rangeInfo.gridCoordinates.first
    val (x2, y2) = rangeInfo.gridCoordinates.second

    val horizontalSpaceBetweenItems = itemContainerWidthPx - itemSize.width

    val additionalStartOffsetX = if (rangeInfo.isStartInGrid) {
        (itemSize.width + horizontalSpaceBetweenItems) / 2f
    } else {
        0f
    }

    val additionalEndOffsetX = if (rangeInfo.isEndInGrid) {
        (itemSize.width + horizontalSpaceBetweenItems) / 2f
    } else {
        itemSize.width + horizontalSpaceBetweenItems
    }


    val startX = x1 * (itemSize.width + horizontalSpaceBetweenItems) + additionalStartOffsetX
    val endX = x2 * (itemSize.width + horizontalSpaceBetweenItems) + additionalEndOffsetX

    val startY = y1 * (itemSize.height + rowsSpacerHeightPx)
    val endY = y2 * (itemSize.height + rowsSpacerHeightPx)

    val itemShapeOutline = dayOfMonthShape.createOutline(
        itemSize,
        layoutDirection,
        Density(density)
    )

    // Draw the first row background
    drawRect(
        color = color,
        topLeft = Offset(startX, startY),
        size = Size(
            width = if (y1 == y2) {
                endX - startX
            } else {
                size.width - startX
            },
            height = itemSize.height
        )
    )

    if (rangeInfo.isStartInGrid) {
        translate(
            left = startX - (itemSize.width / 2),
            top = startY
        ) {
            drawOutline(
                outline = itemShapeOutline,
                color = color
            )
        }
    }

    if (rangeInfo.isEndInGrid) {
        translate(
            left = endX - (itemSize.width / 2),
            top = endY
        ) {
            drawOutline(
                outline = itemShapeOutline,
                color = color
            )
        }
    }

    if (y1 != y2) {
        for (y in y2 - y1 - 1 downTo 1) {
            drawRect(
                color = color,
                topLeft = Offset(
                    x = 0f,
                    y = startY + y * (itemSize.height + rowsSpacerHeightPx)
                ),
                size = Size(
                    width = size.width,
                    height = itemSize.height
                )
            )
        }

        drawRect(
            color = color,
            topLeft = Offset(
                x = 0f,
                y = endY
            ),
            size = Size(
                width = endX,
                height = itemSize.height
            )
        )
    }
}