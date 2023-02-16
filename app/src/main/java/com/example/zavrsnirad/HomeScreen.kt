package com.example.zavrsnirad

import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import android.util.Log
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.zavrsnirad.sealed.DataState
import com.example.zavrsnirad.ui.theme.BGGray
import com.example.zavrsnirad.ui.theme.NormalText
import com.example.zavrsnirad.viewmodels.HomeViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.math.roundToInt

@Suppress("OverrideDeprecatedMigration")
class HomeScreen : ComponentActivity() {

    private lateinit var mAuth: FirebaseAuth

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this ) {
            overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
        }

        mAuth = Firebase.auth

        setContent{
            val systemUiController = rememberSystemUiController()
            systemUiController.setStatusBarColor(
                color = BGGray
            )
            val navController = rememberNavController()

            Scaffold(
                topBar = {},
                bottomBar = {},
            ){paddingVal ->
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingVal),
                    color = BGGray
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.Top
                    ){

                        SetData(viewModel)
                    }
                }
            }
        }
    }

    fun openBalanceChange(){
        startActivity(Intent(this@HomeScreen, BalanceChange::class.java))
        overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
    }

    @Composable
    fun SetData(viewModel: HomeViewModel) {
        when(val result = viewModel.response.value){
            is DataState.Loading->{
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator()
                }
            }
            is DataState.Success->{
                ShowColumn(result.data)
            }
            is DataState.Failure->{
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = result.message,
                        fontSize = 30.sp,
                    )
                }
            }
            else->{
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "Error fetching data!",
                        fontSize = 30.sp
                    )
                }
            }
        }
    }

    @Composable
    fun ShowColumn(data: UserModel) {
        Column(
            modifier = Modifier.fillMaxSize()
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(170.dp)
                ){
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 15.dp, vertical = 20.dp),
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Normal,
                        color = NormalText,
                        text = "Welcome back! ${data.userName}",
                        textAlign = TextAlign.Left
                    )
                }

            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(210.dp)
                        .padding(15.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = 2.dp,
                    backgroundColor = Color.White
                ){

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceAround
                    ){

                        Text(
                            modifier = Modifier
                                .padding(10.dp),
                            text = "Your balance",
                            fontSize = 34.sp,
                            fontWeight = FontWeight.Normal,
                            color = NormalText,
                        )

                        Text(
                            modifier = Modifier
                                .padding(10.dp)
                                .weight(0.8f),
                            text = data.userBalance.toString() + "€",
                            fontSize = 55.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                        )

                    }
                }
            }
        }
    }



    /*



Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(850.dp)
                        .padding(10.dp),
                    shape = RoundedCornerShape(10.dp),
                    elevation = 2.dp,
                    backgroundColor = Color.White
                ){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ){
                        Text(
                            modifier = Modifier
                                .padding(10.dp),
                            text = "Spending analytics",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Normal,
                            color = NormalText,
                        )

                        Spacer(Modifier.height(30.dp))

                        PieChart(
                            data = mapOf(
                                Pair("Food", 12),
                                Pair("Shopping", 25),
                                Pair("Taxi", 8),
                                Pair("Subscriptions", 20),
                                Pair("Dept", 30),
                            )
                        )
                    }
                }
            }
     */

    /*
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
        }*/
}