package com.example.zavrsnirad

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.finishAfterTransition
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.example.zavrsnirad.ui.theme.BGGray
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeScreen : ComponentActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        setContent{
            Main()
        }
    }

    @Override
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
    }

    @Composable
    fun Main(){
        val systemUiController = rememberSystemUiController()
        systemUiController.setStatusBarColor(
            color = BGGray
        )

        val navController = rememberNavController()

        /*
        val user = mAuth.currentUser

        val photoUrl: Uri

        user?.let{
            val photo = user.photoUrl
            photo?.let{
                photoUrl = photo
            }
        }*/

        Scaffold(
            topBar = {TopAppBar(backgroundColor = BGGray, content = {}, elevation = 0.dp)},
            bottomBar = {BottomNavigationBar(navController)},
            content = { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    Navigation(navController = navController)
                }
            },
            backgroundColor = Color.White
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun MainPreview(){
        Main()
    }

    @Composable
    fun Navigation(navController: NavHostController) {
        NavHost(navController, startDestination = NavigationItem.Balance.route) {
            composable(NavigationItem.Balance.route) {
                BalanceScreen()
            }
            composable(NavigationItem.Depts.route) {
                DeptsScreen()
            }
            composable(NavigationItem.Profile.route) {
                ProfileScreen()
            }
        }
    }

    @Composable
    fun BottomNavigationBar(navController: NavController){
        val items = listOf(
            NavigationItem.Balance,
            NavigationItem.Depts,
            NavigationItem.Profile
        )
        BottomNavigation(
            backgroundColor = Color.LightGray,
            elevation = 5.dp,
        ){
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            items.forEach { item ->
                BottomNavigationItem(
                    icon = { Icon(item.icon, contentDescription = item.title) },
                    label = { Text(text = item.title) },
                    selectedContentColor = Color.DarkGray,
                    unselectedContentColor = Color.White,
                    alwaysShowLabel = true,
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}