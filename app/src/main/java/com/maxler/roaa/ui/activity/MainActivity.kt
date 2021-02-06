package com.maxler.roaa.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import com.maxler.roaa.R
import com.maxler.roaa.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

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
            ), drawerLayout = drawerLayout
        )
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupDataBinding()
        setSupportActionBar(findViewById(R.id.toolbar))
        setupDataBinding()
        setupNavigation()
        setupViews()



}



    private fun setupDataBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    private fun setupNavigation(){
        navView.setupWithNavController(navController)
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


        bottom_navigation.setOnNavigationItemSelectedListener {
            NavigationUI.onNavDestinationSelected(it,navController)
        }

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        NavigationUI.onNavDestinationSelected(item, navController)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }




     private fun handlingDrawableOpen(destination: NavDestination?, navController: NavController?) {
        if (destination!!.id  in arrayOf(
                navController!!.graph.startDestination,
                R.id.videoListFragment)
        ){
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }


    private fun handlingBottomNavView(destination: NavDestination){
        if (destination.id == R.id.videoListFragment){
            bottom_navigation.visibility =View.GONE
        }else{
            bottom_navigation.visibility =View.VISIBLE
        }
    }

    private fun setupViews() {
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) ||  super.onSupportNavigateUp()
    }


}








