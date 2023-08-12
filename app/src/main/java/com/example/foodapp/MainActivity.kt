package com.example.foodapp

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu_24)

        navView.setNavigationItemSelectedListener(this)

        if (savedInstanceState==null){
            openFragment(HomeFragment())
            navView.setCheckedItem(R.id.nav_home)
        }

        // Call a function to set user profile details in the NavigationView header
        setupUserProfile()
    }

    private fun setupUserProfile() {
        val headerView = navView.getHeaderView(0)

        val profileImage: CircleImageView = headerView.findViewById(R.id.profile_image)
        val userName = headerView.findViewById<TextView>(R.id.user_name)
        val userEmail = headerView.findViewById<TextView>(R.id.user_email)
        val userPhone = headerView.findViewById<TextView>(R.id.user_phone)

        // Set the user profile details (Replace with actual user data)
        profileImage.setImageResource(R.drawable.baseline_person_24)
        userName.text = "John Doe"
        userEmail.text = "john.doe@example.com"
        userPhone.text = "+1 (555) 123-4567"
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation item clicks here
        when (item.itemId) {
            R.id.nav_profile -> openFragment(ProfileFragment())
            R.id.nav_home -> openFragment(HomeFragment())
            R.id.nav_menu -> openFragment(MenuFragment())
            R.id.nav_orders -> openFragment(OrderFragment())
            R.id.nav_special_offers -> openFragment(SpecialFragment())
            R.id.nav_discount -> openFragment(DiscountFragment())
            R.id.nav_about_us-> openFragment(AboutusFragment())
            R.id.nav_logout -> openFragment(LogFragment())
            // Add more cases for other menu items if needed
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_content, fragment)
            .commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        drawerLayout.openDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
