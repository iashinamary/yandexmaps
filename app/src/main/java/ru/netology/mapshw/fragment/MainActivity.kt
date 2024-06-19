package ru.netology.mapshw.fragment

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.netology.mapshw.R
import ru.netology.mapshw.databinding.ActivityBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.bottomNavigationView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val navController = navHostFragment?.findNavController()
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.mapFragment, R.id.listFragment))
        if (navController != null) {
            navView.setupWithNavController(navController)
        }
        if (navController != null) {
            setupActionBarWithNavController(navController, appBarConfiguration)
        }


    }

}