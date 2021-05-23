package com.rohan.solutiondevelopersassignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.rohan.solutiondevelopersassignment.Authentication.LoginActivity
import com.rohan.solutiondevelopersassignment.Fragments.HomeFragment
import com.rohan.solutiondevelopersassignment.Fragments.ProfileFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.switch_layout.*
import kotlinx.android.synthetic.main.switch_layout.view.*
import java.util.jar.Manifest

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if(FirebaseAuth.getInstance().uid==null) {
            Intent(this, LoginActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(it)
            }
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val actionBar: ActionBar? = this.supportActionBar
        actionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        actionBar?.setCustomView(R.layout.switch_layout)
        supportActionBar?.title = "Registered Users"
        val homeFragment = HomeFragment()
        val profileFragment = ProfileFragment()
        setCurrentFragment(homeFragment)
        tvActionTitle.text = "Registered Users"
        switchForActionBar.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        bnvHome.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    tvActionTitle.text = "Registered Users"
                    switchForActionBar.visibility = View.VISIBLE
                    imgMoon.visibility = View.VISIBLE
                    imgSun.visibility = View.VISIBLE
                    setCurrentFragment(homeFragment)
                    true
                }
                R.id.menu_profile -> {
                    tvActionTitle.text = "Your Profile"
                    switchForActionBar.visibility = View.GONE
                    imgMoon.visibility = View.GONE
                    imgSun.visibility = View.GONE
                    setCurrentFragment(profileFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flHome,fragment)
            commit()
        }
}