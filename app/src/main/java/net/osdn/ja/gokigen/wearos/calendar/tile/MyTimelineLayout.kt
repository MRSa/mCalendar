package net.osdn.ja.gokigen.wearos.calendar.tile

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.DeviceParametersBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.material.layouts.PrimaryLayout
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tooling.preview.devices.WearDevices
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

        // フォントサイズがどうなっているか知る (0.94 / 1.00 / 1.06 / 1.12 / 1.18 / 1.24 )
        val fontScale: Float = context.resources.configuration.fontScale
        // Log.v("TAG", " - - - - - - - - - fontScale : $fontScale")

        if (fontScale > 1.09f)
        {
            //  フォントスケールが大きい場合には、年-月の表示を省略して画面内に収めるよう調整する
            return (
                    PrimaryLayout.Builder(deviceParameters)
                        .setPrimaryLabelTextContent(
                            // CONTENT
                            monthlyCalendarLayout.getMonthlyCalendarLayout(clickable = launchActivity, fontScale = fontScale)
                        )
                        .setVerticalSpacerHeight(0.15f)
                        .build()
                    )
        }
        return (
                PrimaryLayout.Builder(deviceParameters)
                    .setPrimaryLabelTextContent(
                        // TITLE
                        monthlyCalendarLayout.getMonthlyCalendarTitleLayout(clickable = launchActivity)
                    )
                    .setSecondaryLabelTextContent(
                        // CONTENT
                        monthlyCalendarLayout.getMonthlyCalendarLayout(clickable = launchActivity, fontScale = fontScale)
                    )
                    .setVerticalSpacerHeight(0.5f)
                    .build()
                )
    }
}

@Preview(
    device = WearDevices.SMALL_ROUND,
    showSystemUi = true,
    backgroundColor = 0xff000000,
    showBackground = true
)

@Composable
fun TilePreview()
{
    val deviceParameters = DeviceParametersBuilders.DeviceParameters.Builder().build()  // ダミー
    val clickable = ModifiersBuilders.Clickable.Builder().build()  // ダミー
    val fontScale = 1.0f  // ダミー
    val monthlyCalendarLayout = MonthlyCalendarElement(LocalContext.current)
    val timeline = PrimaryLayout.Builder(deviceParameters)
        .setPrimaryLabelTextContent(
            // TITLE
            monthlyCalendarLayout.getMonthlyCalendarTitleLayout(clickable)
        )
        .setSecondaryLabelTextContent(
            // CONTENT
            monthlyCalendarLayout.getMonthlyCalendarLayout(clickable, fontScale)
        )
        .build()
    LayoutRootPreview(root = timeline)
}
