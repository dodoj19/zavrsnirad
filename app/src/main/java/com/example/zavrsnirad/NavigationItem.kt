package com.example.zavrsnirad

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(var route: String, var icon: ImageVector, var title: String) {
    object Balance : NavigationItem("balance", Icons.Filled.AccountBalance, "Balance")
    object Depts : NavigationItem("depts", Icons.Filled.AccountBalanceWallet, "Depts")
    object Profile : NavigationItem("profile", Icons.Filled.AccountCircle, "Profile")
}