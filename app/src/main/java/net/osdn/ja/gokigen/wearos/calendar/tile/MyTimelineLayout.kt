package net.osdn.ja.gokigen.wearos.calendar.tile

import android.content.Context
import androidx.wear.protolayout.DeviceParametersBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.material.ChipColors
import androidx.wear.protolayout.material.Colors
import androidx.wear.protolayout.material.CompactChip
import androidx.wear.protolayout.material.layouts.PrimaryLayout

class MyTimelineLayout
{
    fun getLayout(context: Context): LayoutElementBuilders.LayoutElement
    {
        return (tileLayout(context))
    }
    private fun tileLayout(context: Context): LayoutElementBuilders.LayoutElement
    {
        val theme = Colors(
            /*primary=*/ 0xFFD0BCFF.toInt(), /*onPrimary=*/ 0xFF381E72.toInt(),
            /*surface=*/ 0xFF202124.toInt(), /*onSurface=*/ 0xFFFFFFFF.toInt()
        )
        val deviceParameters = DeviceParametersBuilders.DeviceParameters.Builder().build()
        val clickable = ModifiersBuilders.Clickable.Builder().build()
        val chipColors = ChipColors.primaryChipColors(theme)
        val timeline =
            PrimaryLayout.Builder(deviceParameters)
                .setPrimaryChipContent(
                    CompactChip.Builder(context, "More",  clickable, deviceParameters)
                        .setChipColors(chipColors)
                        .build()
                )
                .build()
        return (timeline)
    }
}
