package com.dxp.micircle.presentation.dashboard

import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

@BindingAdapter(value = ["fragmentManager", "navHostId"], requireAll = true)
fun BottomNavigationView.bindWithNavHost(fragmentManager: FragmentManager, navHostId: Int) {
    val navController = (fragmentManager.findFragmentById(navHostId) as NavHostFragment).navController
    setupWithNavController(navController)
}