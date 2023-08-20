package net.osdn.ja.gokigen.wearos.calendar.presentation

import android.Manifest
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import net.osdn.ja.gokigen.wearos.calendar.DbSingleton
import net.osdn.ja.gokigen.wearos.calendar.R
import net.osdn.ja.gokigen.wearos.calendar.importer.IntentDataImporter
import net.osdn.ja.gokigen.wearos.calendar.presentation.ui.ViewRoot
import java.io.File

class MainActivity : ComponentActivity()
{
    private lateinit var rootComponent : ViewRoot
    private val storageDao = DbSingleton.db.storageDao()
    override fun onCreate(savedInstanceState: Bundle?)
    {
        try
        {
            ///////// SHOW SPLASH SCREEN : call before super.onCreate() /////////
            installSplashScreen()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        super.onCreate(savedInstanceState)

        try
        {
            if (!allPermissionsGranted())
            {
                val requestPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                    if(!allPermissionsGranted())
                    {
                        Toast.makeText(this, getString(R.string.permission_not_granted), Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                requestPermission.launch(REQUIRED_PERMISSIONS)
            }
            else
            {
                setupEnvironments()
            }
            rootComponent = ViewRoot(applicationContext)
            setContent {
                rootComponent.Content()
            }
        }
        catch (ex: Exception)
        {
            ex.printStackTrace()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setupEnvironments()
    {
        val thread = Thread {
            try
            {
                val subDirectory = File(filesDir, CACHE_DIR)
                if (!subDirectory.exists())
                {
                    if (!subDirectory.mkdirs())
                    {
                        outputDebugLog("----- Sub Directory $CACHE_DIR create failure...")
                    }
                }
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
        }
        try
        {
            thread.start()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    /**
     *
     */
    override fun onResume()
    {
        super.onResume()

        // インテントを取得して処理する (ACTION_SENDの時)
        val intent = intent
        val action = intent.action
        outputDebugLog("onResume() : $action")
        if (action != null)
        {
            try
            {
                if (Intent.ACTION_SEND == action)
                {
                    val flags = intent.flags
                    val checkFlags = FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY // 既に起動済の場合は処理しない
                    outputDebugLog("INTENT : $intent")
                    if ((flags.and(checkFlags)) == 0)
                    {
                        outputDebugLog(" DATA IMPORT ")
                        importReceivedIntent()
                    }
                }
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
        }

        if (isDebugLog)
        {
            // 休日データのリストを確認 (デバッグ用)
            try
            {
                val thread = Thread {
                    val count = storageDao.getCount()
                    outputDebugLog("  ANNIVERSARY/HOLIDAY DATA COUNT : $count ")
                }
                thread.start()
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
        }
    }

    private fun importReceivedIntent()
    {
        try
        {
            val thread = Thread {
                // 取得したSENDインテントを処理する
                val ret = IntentDataImporter(this.applicationContext, intent).start()
                val title = intent.getStringExtra(Intent.EXTRA_SUBJECT)
                runOnUiThread {
                    Toast.makeText(this, getString(R.string.data_imported) + " "  + ret + " " + title, Toast.LENGTH_SHORT).show()
                    // 処理が終わると、アプリケーションは終了する
                    finish()
                }
            }
            thread.start()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun outputDebugLog(data: String)
    {
        if (isDebugLog)
        {
            runOnUiThread {
                Log.v(TAG, data)
            }
        }
    }

    companion object
    {
        private val TAG = MainActivity::class.java.simpleName
        private const val CACHE_DIR : String = "/caches/"
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.VIBRATE,
            Manifest.permission.WAKE_LOCK,
        )
        private const val isDebugLog = true
    }
}
