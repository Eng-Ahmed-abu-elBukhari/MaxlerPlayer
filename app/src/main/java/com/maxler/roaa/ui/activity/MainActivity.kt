package com.maxler.roaa.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.maxler.roaa.R
import com.maxler.roaa.databinding.ActivityMainBinding


private const val READ_EXTERNAL_STORAGE_REQUEST = 1

class MainActivity : AppCompatActivity() ,NavigationView.OnNavigationItemSelectedListener{


    /**You’ll use navController to navigate from one fragment to another.
     * Import findNavController from androidx.navigation.findNavController. */
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment).navController
    }

    //2
    /** appBarConfiguration defines which fragments are the top level fragments so the drawerLayout
     * and hamburger icon can work properly.*/
    private val appBarConfiguration by lazy {
        AppBarConfiguration(
            navController.graph,
            drawerLayout = binding.drawerLayout
        )
    }


    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
    }

    private val toolbar by lazy {
        findViewById<Toolbar>(R.id.toolbar)
    }

    private val fab by lazy {
        findViewById<FloatingActionButton>(R.id.fab)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(toolbar)
        setupNavigation()
        requestPermission()

}




    private fun setupNavigation(){
        binding.navView.setupWithNavController(navController)
        setupActionBarWithNavController(navController,appBarConfiguration)
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)


        navController.addOnDestinationChangedListener { controller: NavController, destination: NavDestination, arguments: Bundle? ->
            when(destination.id){
                navController.graph.startDestination->{
                    fab.show()
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    toolbar.visibility = View.VISIBLE
                }
                else->{
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
            }
        }


        binding.navView.setNavigationItemSelectedListener(this)


        fab.setOnClickListener {
            navController.navigate(R.id.playlistsFragment)
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        NavigationUI.onNavDestinationSelected(item, navController)
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) ||  super.onSupportNavigateUp()
    }







    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when (requestCode){
            READ_EXTERNAL_STORAGE_REQUEST->{
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"Permission Granted",Toast.LENGTH_LONG).show()
                }else{
                    val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )


                    if (showRationale){
                        showPermissionMessage()
                    }else{
                        goToSetting()
                    }

                }
                return
            }

        }



        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }







    private fun haveStoragePermission() =
        ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED


    private fun requestPermission(){
        if (!haveStoragePermission()){
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

            ActivityCompat.requestPermissions(
                this,
                permissions,
                READ_EXTERNAL_STORAGE_REQUEST)
        }
    }


    private fun goToSetting(){
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,Uri.parse(
            "package:$packageName")).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also {
            startActivity(it)
        }
    }



    private fun showPermissionMessage(){
        MaterialAlertDialogBuilder(this)
            .setTitle("Permission Access")
            .setMessage("App requires access to storage to access images stored in device.")
            .setNegativeButton("CANCEl"){ dialog, which ->
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_LONG).show()
            }
            .setPositiveButton("GRANTED"){dialog,which->
                requestPermission()
            }
            .show()
    }
}
