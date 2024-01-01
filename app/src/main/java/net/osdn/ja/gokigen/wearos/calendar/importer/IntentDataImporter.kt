package net.osdn.ja.gokigen.wearos.calendar.importer

import android.content.Context
import android.content.Intent
import android.util.Log
import net.osdn.ja.gokigen.wearos.calendar.DbSingleton
import net.osdn.ja.gokigen.wearos.calendar.R
import net.osdn.ja.gokigen.wearos.calendar.storage.DataContent

enum class ImportCommandStatus {
    NORMAL, IMPORT_HOLIDAY, IMPORT_ANNIVERSARY, IMPORT_NOTIFY, IMPORT_EVENT
}
class IntentDataImporter(private val context: Context, val intent: Intent)
{
    private val storageDao = DbSingleton.db.storageDao()

    fun start() : String
    {
        var detail = context.getString(R.string.not_supported)
        try
        {
            val title = intent.getStringExtra(Intent.EXTRA_SUBJECT) ?: ""
            val data = intent.getStringExtra(Intent.EXTRA_TEXT) ?: ""
            detail = parseIntent(title, data, detail)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (detail)
    }

    private fun parseIntent(title: String, data: String, defaultReplyMessage: String) : String
    {
        var currentCommand: ImportCommandStatus = ImportCommandStatus.NORMAL
        var reply = defaultReplyMessage
        try
        {
            Log.v(TAG, " ===== parseIntent: $title")

            val lines = data.split("\r?\n".toRegex())
            for ((currentLine, line) in lines.withIndex())
            {
                if (currentLine == 0)
                {
                    if ((!line.contains("mCalendar"))||(!line.contains("Definition"))||(!line.contains("Data")))
                    {
                        // 先頭行 に mCalendar Data Definition という文字列が含まれているときのみ解析を行う。
                        Log.v(TAG, "Imported data is not valid...")
                        return (reply)
                    }
                    continue
                }
                if (currentLine == 1)
                {
                    // 2行目はバージョン情報などで利用（予約領域）
                    continue
                }
                //  3行目以降はコマンドデータとする
                currentCommand = parseDataLine(line.lowercase(), currentCommand)
            }
            reply = context.getString(R.string.import_success)
        }
        catch (e: java.lang.Exception)
        {
            e.printStackTrace()
            reply = context.getString(R.string.detect_error)
        }
        return (reply)
    }

    private fun parseDataLine(lineData: String, currentStatus: ImportCommandStatus): ImportCommandStatus
    {
        try
        {
            if (currentStatus == ImportCommandStatus.NORMAL)
            {
                if (checkDeleteCommand(lineData))
                {
                    return (currentStatus)
                }
                if ((lineData.contains("import"))&&(lineData.contains("data"))&&(lineData.contains("start")))
                {
                    if (lineData.contains("holiday"))
                    {
                        return (ImportCommandStatus.IMPORT_HOLIDAY)
                    }
                    if (lineData.contains("anniversary"))
                    {
                        return (ImportCommandStatus.IMPORT_ANNIVERSARY)
                    }
                    if (lineData.contains("notify"))
                    {
                        return (ImportCommandStatus.IMPORT_NOTIFY)
                    }
                    if (lineData.contains("event"))
                    {
                        return (ImportCommandStatus.IMPORT_EVENT)
                    }
                }
                return (currentStatus)
            }
            if ((lineData.contains("import"))&&(lineData.contains("data"))&&(lineData.contains("end")))
            {
                return (ImportCommandStatus.NORMAL)
            }
            importLineData(lineData, currentStatus)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (currentStatus)
    }

    private fun importLineData(lineData: String, currentStatus: ImportCommandStatus)
    {
        try
        {
            val attribute = when (currentStatus) {
                ImportCommandStatus.NORMAL -> 0
                ImportCommandStatus.IMPORT_HOLIDAY -> 1
                ImportCommandStatus.IMPORT_ANNIVERSARY -> 2
                ImportCommandStatus.IMPORT_NOTIFY -> 3
                ImportCommandStatus.IMPORT_EVENT -> 4
            }
            var year = -1
            var month = 0
            var date = 0
            val lineItems = lineData.split(",".toRegex())
            val dataName = lineItems[1]
            val dateItem = lineItems[0].replace("[\\s　]".toRegex(), "")
            val dateItems = dateItem.split("[-/]".toRegex())
            if (dateItems.size == 2)
            {
                // 月・日の指定だった場合
                year = -1
                month = dateItems[0].toInt()
                date = dateItems[1].toInt()
            }
            else if (dateItems.size >= 3)
            {
                // 年・月・日の指定があった場合
                year = dateItems[0].toInt()
                month = dateItems[1].toInt()
                date = dateItems[2].toInt()
            }
            val content = DataContent.create(attribute, year, month, date, dataName, "")
            storageDao.insertAll(content)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun checkDeleteCommand(lineData: String): Boolean
    {
        try
        {
            if ((lineData.contains("delete all"))&&(lineData.contains("confirm")))
            {
                if (lineData.contains("only"))
                {
                    if (lineData.contains("holiday"))
                    {
                        // 休日のみ削除
                        outputDebugLog("DELETE ALL ONLY HOLIDAY DATA")
                        storageDao.deleteAllAttribute(1)
                        return (true)
                    }
                    if (lineData.contains("anniversary"))
                    {
                        // 記念日のみ削除
                        outputDebugLog("DELETE ALL ONLY ANNIVERSARY DATA")
                        storageDao.deleteAllAttribute(2)
                        return (true)
                    }
                    if (lineData.contains("notify"))
                    {
                        //　通知１のみ削除
                        outputDebugLog("DELETE ALL ONLY NOTIFY DATA")
                        storageDao.deleteAllAttribute(3)
                        return (true)
                    }
                    if (lineData.contains("event"))
                    {
                        // 通知２のみ削除
                        outputDebugLog("DELETE ALL ONLY EVENT DATA")
                        storageDao.deleteAllAttribute(4)
                        return (true)
                    }
                    // 検出できなかった場合は、全件削除
                }
                // データを全件削除
                outputDebugLog("DELETE ALL DATA")
                storageDao.deleteAll()
                return (true)
            }
            if ((lineData.contains("delete before"))&&(lineData.contains("confirm")))
            {
                val startIndex = lineData.indexOf("delete before") + "delete before".length
                val endIndex = lineData.indexOf("confirm")
                val dateString = lineData.substring(startIndex, endIndex)
                val targetData = dateString.replace("[\\s　]".toRegex(), "")
                val lines = targetData.split("[-/]".toRegex())
                if (lines.size >= 2)
                {
                    val yearInt = lines[0].toInt()
                    val monthInt = lines[1].toInt()
                    outputDebugLog("DELETE BEFORE $yearInt-$monthInt")
                    storageDao.deleteBeforeByYear(yearInt)
                    storageDao.deleteBeforeByMonth(yearInt, monthInt)
                }
                return (true)
            }
            if (lineData.contains("delete after"))
            {
                val startIndex = lineData.indexOf("delete after") + "delete after".length
                val endIndex = lineData.indexOf("confirm")
                val dateString = lineData.substring(startIndex, endIndex)
                val targetData = dateString.replace("[\\s　]".toRegex(), "")
                val lines = targetData.split("[-/]".toRegex())
                if (lines.size >= 2)
                {
                    val yearInt = lines[0].toInt()
                    val monthInt = lines[1].toInt()
                    outputDebugLog("DELETE AFTER $yearInt-$monthInt")
                    storageDao.deleteAfterByYear(yearInt)
                    storageDao.deleteAfterByMonth(yearInt, monthInt)
                }
                return (true)
            }
            if (lineData.contains("delete month"))
            {
                val startIndex = lineData.indexOf("delete month") + "delete month".length
                val endIndex = lineData.indexOf("confirm")
                val monthString = lineData.substring(startIndex, endIndex)
                val targetData = monthString.replace("[\\s　]".toRegex(), "")
                val monthInt = targetData.toInt()
                if (lineData.contains("only holiday"))
                {
                    // 休日のみ削除
                    outputDebugLog("DELETE MONTH HOLIDAY DATA $monthInt")
                    storageDao.deleteMonthOnlyAttribute(monthInt, 1)
                    return (true)
                }
                if (lineData.contains("only anniversary"))
                {
                    // 記念日のみ削除
                    outputDebugLog("DELETE MONTH ANNIVERSARY DATA $monthInt")
                    storageDao.deleteMonthOnlyAttribute(monthInt, 2)
                    return (true)
                }
                if (lineData.contains("only notify"))
                {
                    // NOTIFYのみ削除
                    outputDebugLog("DELETE MONTH NOTIFY DATA $monthInt")
                    storageDao.deleteMonthOnlyAttribute(monthInt, 3)
                    return (true)
                }
                if (lineData.contains("only event"))
                {
                    // EVENTのみ削除
                    outputDebugLog("DELETE MONTH EVENT DATA $monthInt")
                    storageDao.deleteMonthOnlyAttribute(monthInt, 4)
                    return (true)
                }
                outputDebugLog("DELETE MONTH $monthInt")
                storageDao.deleteMonthOnly(monthInt)
                return (true)
            }
            if (lineData.contains("delete only"))
            {
                val startIndex = lineData.indexOf("delete only") + "delete only".length
                val endIndex = lineData.indexOf("confirm")
                val dateString = lineData.substring(startIndex, endIndex)
                val targetData = dateString.replace("[\\s　]".toRegex(), "")
                val lines = targetData.split("[-/]".toRegex())
                if (lines.size >= 2)
                {
                    val yearInt = lines[0].toInt()
                    val monthInt = lines[1].toInt()
                    outputDebugLog("DELETE ONLY $yearInt-$monthInt")
                    storageDao.deleteYearMonth(yearInt, monthInt)
                }
                return (true)
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (false)
    }

    private fun outputDebugLog(data: String)
    {
        if (isDebugLog) { Log.v(TAG, data) }
    }
    companion object
    {
        private val TAG = IntentDataImporter::class.java.simpleName
        private const val isDebugLog = true
    }
}
