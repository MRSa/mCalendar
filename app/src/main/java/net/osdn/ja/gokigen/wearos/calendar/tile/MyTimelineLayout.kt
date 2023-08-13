package net.osdn.ja.gokigen.wearos.calendar.tile

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.DeviceParametersBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.material.layouts.PrimaryLayout
import androidx.wear.tiles.RequestBuilders
import com.google.android.horologist.compose.tools.LayoutRootPreview

class MyTimelineLayout
{
    fun getLayout(context: Context, requestParams: RequestBuilders.TileRequest): LayoutElementBuilders.LayoutElement
    {
        // 起動したデバイスの情報
        val deviceParameters = requestParams.deviceConfiguration

        // CLICKした時にオープンするACTIVITYの設定 (自分自身を起動する)
        val targetActivity = ActionBuilders.AndroidActivity.Builder()
            .setPackageName("net.osdn.ja.gokigen.wearos.calendar")
            .setClassName("net.osdn.ja.gokigen.wearos.calendar.presentation.MainActivity")
            .build()

        // クリック実行時の処理 (ACTIVITYを起動する)
        val launchActivity = ModifiersBuilders.Clickable.Builder()
            .setId("calendar")
            .setOnClick(ActionBuilders.LaunchAction.Builder()
                .setAndroidActivity(targetActivity)
                .build()
            )
            .build()

        // 月間カレンダーのレイアウトを表示する
        val monthlyCalendarLayout = MonthlyCalendarElement(context)
        return (
                PrimaryLayout.Builder(deviceParameters)
                    .setPrimaryLabelTextContent(
                        // TITLE
                        monthlyCalendarLayout.getMonthlyCalendarTitleLayout(clickable = launchActivity)
                    )
                    .setSecondaryLabelTextContent(
                        // CONTENT
                        monthlyCalendarLayout.getMonthlyCalendarLayout(clickable = launchActivity)
                    )
                    .build()
                )
    }
}

@Preview(
    device = Devices.WEAR_OS_SMALL_ROUND,
    showSystemUi = true,
    backgroundColor = 0xff000000,
    showBackground = true
)

@Composable
fun TilePreview()
{
    val deviceParameters = DeviceParametersBuilders.DeviceParameters.Builder().build()  // ダミー
    val clickable = ModifiersBuilders.Clickable.Builder().build()  // ダミー
    val monthlyCalendarLayout = MonthlyCalendarElement(LocalContext.current)
    val timeline = PrimaryLayout.Builder(deviceParameters)
        .setPrimaryLabelTextContent(
            // TITLE
            monthlyCalendarLayout.getMonthlyCalendarTitleLayout(clickable)
        )
        .setSecondaryLabelTextContent(
            // CONTENT
            monthlyCalendarLayout.getMonthlyCalendarLayout(clickable)
        )
        .build()
    LayoutRootPreview(root = timeline)
}
