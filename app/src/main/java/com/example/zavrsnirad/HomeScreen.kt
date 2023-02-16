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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
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
import com.example.zavrsnirad.ui.theme.Purple500
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
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
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
                        .size(180.dp)
                ){
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 15.dp, vertical = 10.dp),
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
                        .requiredHeight(180.dp)
                        .padding(horizontal = 10.dp, vertical = 10.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = 2.dp,
                    backgroundColor = Color.White
                ){

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ){

                        Text(
                            modifier = Modifier,
                            text = "Your balance",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Normal,
                            color = NormalText,
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.Bottom
                        ){
                            Text(
                                modifier = Modifier
                                    .weight(0.8f),
                                text = data.userBalance.toString() + "€",
                                fontSize = 52.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black,
                            )

                            /*

                            OutlinedButton(
                                modifier = Modifier.size(50.dp),
                                border = BorderStroke(3.dp, Color.DarkGray),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = Color.DarkGray),
                                onClick = { /*TODO*/ }
                            ) {
                                Text(
                                    text = "+",
                                    color = Color.DarkGray,
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.W900
                                )
                            }

                            */

                            OutlinedButton(
                                modifier = Modifier.size(height= 40.dp, width = 100.dp),
                                border = BorderStroke(2.dp, Color.DarkGray),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = Color.DarkGray),
                                onClick = { /*TODO*/ }
                            ) {
                                Text(
                                    text = "NEW",
                                    color = Color.DarkGray,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.W900
                                )
                                Icon(
                                    Icons.Filled.Add,
                                    null,
                                    Modifier,
                                    Color.DarkGray
                                )
                            }
                        }

                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(330.dp)
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    shape = RoundedCornerShape(12.dp),
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
                            text = "Spending",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Normal,
                            color = NormalText,
                        )
                        Text(
                            modifier = Modifier
                                .padding(10.dp),
                            text = "This month",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W600,
                            color = NormalText,
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(15.dp),
                            contentAlignment = Alignment.Center,

                        ){
                            val dataList = mutableListOf(450, 230, 549, 204, 659, 1023, 543, 154, 92, 543, 354)
                            val floatValue = mutableListOf<Float>()
                            val datesList = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)

                            dataList.forEachIndexed { index, value ->

                                floatValue.add(index = index, element = value.toFloat()/dataList.max().toFloat())

                            }

                            BarGraph(
                                graphBarData = floatValue,
                                xAxisScaleData = datesList,
                                barData_ = dataList,
                                height = 140.dp,
                                roundType = BarType.TOP_CURVED,
                                barWidth = 15.dp,
                                barColor = Purple500,
                                barArrangement = Arrangement.SpaceEvenly
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(220.dp)
                        .padding(horizontal = 10.dp, vertical = 10.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = 2.dp,
                    backgroundColor = Color.White
                ){

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ){

                            Text(
                                modifier = Modifier
                                    .padding(5.dp),
                                text = "Transactions",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Normal,
                                color = NormalText,
                            )

                            OutlinedButton(
                                modifier = Modifier,
                                border = BorderStroke(0.dp, Color.White),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = BGGray, contentColor = Color.LightGray),
                                onClick = { /*TODO*/ }
                            ) {
                                Text(
                                    text = "VIEW ALL",
                                    modifier = Modifier,
                                    color = Color.DarkGray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black
                                )

                                Spacer(Modifier.width(5.dp))

                                Icon(
                                    Icons.Filled.Search,
                                    null,
                                    Modifier,
                                    Color.DarkGray
                                )
                            }

                            /*
                            ClickableText(
                                modifier = Modifier
                                    .padding(5.dp),
                                text = AnnotatedString("VIEW ALL", paragraphStyle = ParagraphStyle(
                                    TextAlign.Right, textDirection = TextDirection.Content)),
                                onClick = {}
                            )*/
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ){

                            Column(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxSize()
                                    .background(color = BGGray, RoundedCornerShape(12.dp)),
                            ){

                            }
                        }
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