package com.maxler.roaa.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.maxler.roaa.R
import com.maxler.roaa.databinding.ActivityMainBinding


private const val READ_EXTERNAL_STORAGE_REQUEST = 1

class MainActivity : AppCompatActivity() ,NavigationView.OnNavigationItemSelectedListener{


    /**You’ll use navController to navigate from one fragment to another.
     * Import findNavController from androidx.navigation.findNavController. */
    private val navController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navHostFragment.navController
    }

    //2
    /** appBarConfiguration defines which fragments are the top level fragments so the drawerLayout
     * and hamburger icon can work properly.*/
    private val appBarConfiguration by lazy {

        AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.videoListFragment
            ), drawerLayout = binding.drawerLayout
        )
    }

    private lateinit var binding :ActivityMainBinding



    private val toolbar by lazy {
        findViewById<Toolbar>(R.id.toolbar)
    }

    private val bottomNavigationView by lazy {
        findViewById<BottomNavigationView>(R.id.bottom_navigation)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupDataBinding()
        setSupportActionBar(findViewById(R.id.toolbar))
        setupNavigation()
        setupViews()
        requestPermission()

}



    private fun setupDataBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }


    private fun setupNavigation(){
        binding.navView.setupWithNavController(navController)
        setupActionBarWithNavController(navController,appBarConfiguration)
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)



        navController.addOnDestinationChangedListener { controller: NavController, destination: NavDestination, arguments: Bundle? ->

            if (destination.id == R.id.homeFragment
            ){
                toolbar.visibility  = View.VISIBLE
            }else{
                toolbar.visibility  = View.GONE
            }

            handlingDrawableOpen(destination = destination, navController = navController)
            handlingBottomNavView(destination = destination)
        }


        bottomNavigationView.setOnNavigationItemSelectedListener {
            NavigationUI.onNavDestinationSelected(it,navController)
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




     private fun handlingDrawableOpen(destination: NavDestination?, navController: NavController?) {
        if (destination!!.id  in arrayOf(
                navController!!.graph.startDestination,
                R.id.videoListFragment)
        ){
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        } else {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
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


    private fun handlingBottomNavView(destination: NavDestination){
        if (destination.id == R.id.videoListFragment){
            bottomNavigationView.visibility =View.GONE
        }else{
            bottomNavigationView.visibility =View.VISIBLE
        }

    }

    private fun setupViews() {
        binding.navView.setNavigationItemSelectedListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) ||  super.onSupportNavigateUp()
    }







    private fun haveStoragePermission() =
        ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED



    private fun requestPermission(){
        if (!haveStoragePermission()){
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)

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
