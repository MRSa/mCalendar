package net.osdn.ja.gokigen.wearos.calendar.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import net.osdn.ja.gokigen.wearos.calendar.R
import net.osdn.ja.gokigen.wearos.calendar.presentation.ui.ViewRoot
import java.io.File

class MainActivity : ComponentActivity()
{
    private lateinit var rootComponent : ViewRoot
    // private val storageDao = DbSingleton.db.storageDao()
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
                        Log.v(TAG, "----- Sub Directory $CACHE_DIR create failure...")
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

    companion object
    {
        private val TAG = MainActivity::class.java.simpleName
        private const val CACHE_DIR : String = "/caches/"
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.VIBRATE,
            Manifest.permission.WAKE_LOCK,
        )
        //private const val isDebugLog = true
    }
}
