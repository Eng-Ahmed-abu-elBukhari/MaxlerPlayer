package com.play.maxler.presentation.screens.permission

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.play.maxler.common.data.Constants
import com.play.maxler.databinding.ActivityPermissionBinding
import com.play.maxler.presentation.screens.main.MainActivity
import com.play.maxler.common.utils.Utils
import com.play.maxler.utils.launchScreen

class PermissionActivity : AppCompatActivity() {

    private lateinit var permissionBinding: ActivityPermissionBinding
    private val  REQUEST_ID_CODE: Int = 101


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionBinding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(permissionBinding.root)
        grantPermission()
    }

    /* user come back from ACTION_APPLICATION_DETAILS_SETTINGS
       to PermissionActivity if user gives permission
       check Permission Granted before activity be in forground*/
/*    override fun onStart() {
        super.onStart()
        if (Utils.isPermissionGranted(Constants.cameraPermission,this))
            launchToMainActivity()
    }*/

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onRestart() {
        super.onRestart()
        if (Utils.isPermissionGranted(permission = Constants.permissions,this))
            launchToMainActivity()

    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun grantPermission(){
        permissionBinding.btnGrant.setOnClickListener {
            requestPermissions(
                Constants.permissions,
                REQUEST_ID_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()){
            when(requestCode){
                REQUEST_ID_CODE->{
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        launchToMainActivity()
                    else
                       showAlertDialogPermission()
                }
            }
        }
    }

    private fun showAlertDialogPermission(){
        AlertDialog.Builder(this)
            .setTitle("Permission Denied")
            .setMessage("Permission to access internet is permanently denied. you need to go to setting to allow the permission.")
            .setNegativeButton("Cancel"){ dialog: DialogInterface?, which: Int ->
                /**  when user click cancel on dialog in permission activity
                 * and come back to prev activity on boarding if destroyed
                 *  then finish take in back to out of app
                 * */
                finish()
            }
                // take in in details screen for app
            .setPositiveButton("Ok"){ dialog: DialogInterface?, which: Int ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.data = Uri.fromParts("package",packageName,null)
                startActivity(intent)
            }.show()
    }

    private fun launchToMainActivity() {
        Intent().launchScreen(this, MainActivity())
        finish()
    }


}