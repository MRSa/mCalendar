package net.osdn.ja.gokigen.wearos.calendar.presentation.ui

import androidx.compose.foundation.ScrollState
import androidx.wear.compose.material.PositionIndicatorState
import androidx.wear.compose.material.PositionIndicatorVisibility

class MyPositionIndicatorState(var scrollState: ScrollState) : PositionIndicatorState
{
    override val positionFraction: Float
        get() {
            return if (scrollState.maxValue == 0) {
                0f
            } else {
                scrollState.value.toFloat() / scrollState.maxValue
            }
        }

    override fun sizeFraction(scrollableContainerSizePx: Float) =
        if (scrollableContainerSizePx + scrollState.maxValue == 0.0f)
        {
            1.0f
        }
        else
        {
            scrollableContainerSizePx / (scrollableContainerSizePx + scrollState.maxValue)
        }

    override fun visibility(scrollableContainerSizePx: Float): PositionIndicatorVisibility {
        return (PositionIndicatorVisibility.Show)
    }

    override fun equals(other: Any?): Boolean {
        return (other as? MyPositionIndicatorState)?.scrollState == scrollState
    }
    override fun hashCode(): Int {
        return scrollState.hashCode()
    }

}
