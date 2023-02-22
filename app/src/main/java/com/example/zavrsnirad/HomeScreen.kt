package com.example.zavrsnirad

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zavrsnirad.sealed.DataState
import com.example.zavrsnirad.ui.theme.AcceptButtonBG
import com.example.zavrsnirad.ui.theme.BGGray
import com.example.zavrsnirad.ui.theme.NormalText
import com.example.zavrsnirad.ui.theme.Purple500
import com.example.zavrsnirad.viewmodels.HomeViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

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

            Scaffold(
                topBar = {
                },
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
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround
        ){

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(60.dp)
                    .padding(5.dp),
                horizontalArrangement = Arrangement.End
            ){
                IconButton(onClick = {
                    startActivity(Intent(this@HomeScreen, ProfileActivity::class.java))
                    overridePendingTransition(com.google.android.material.R.anim.abc_fade_in, com.google.android.material.R.anim.abc_fade_out)
                }) {
                    Icon(
                        Icons.Filled.AccountCircle,
                        null,
                        Modifier.size(48.dp),
                        Color.DarkGray
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(140.dp)
                ){
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 15.dp, vertical = 10.dp),
                        fontSize = 38.sp,
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
                        .requiredHeight(220.dp)
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
                            text = "Your balance:",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Normal,
                            color = NormalText,
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.Bottom
                        ){

                            val df = DecimalFormat("#.##")
                            df.roundingMode = RoundingMode.DOWN
                            val roundoff = df.format(data.userBalance)

                            Text(
                                modifier = Modifier
                                    .weight(0.8f),
                                text = roundoff.toString() + "€",
                                fontSize = 52.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black,
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            horizontalArrangement = Arrangement.End
                        ){
                            OutlinedButton(
                                modifier = Modifier.size(height= 40.dp, width = 90.dp),
                                border = BorderStroke(2.dp, Color(0xFF388E3C)),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = Color.DarkGray),
                                onClick = {
                                    val newIntent = Intent(this@HomeScreen, BalanceChange::class.java)
                                    newIntent.putExtra("TransactionType", "Add")

                                    startActivity(newIntent)

                                    overridePendingTransition(com.google.android.material.R.anim.abc_popup_enter, com.google.android.material.R.anim.abc_popup_exit)
                                    finishAfterTransition()
                                }
                            ) {

                                Text(
                                    text = "ADD",
                                    color = Color(0xFF388E3C),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W900
                                )

                                /*
                                Icon(
                                    Icons.Filled.Add,
                                    null,
                                    Modifier,
                                    Color.DarkGray
                                )*/
                            }

                            Spacer(Modifier.width(5.dp))

                            OutlinedButton(
                                modifier = Modifier.size(height= 40.dp, width = 90.dp),
                                border = BorderStroke(2.dp, Color(0xFFB71C1C)),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = Color.DarkGray),
                                onClick = {
                                    val newIntent = Intent(this@HomeScreen, BalanceChange::class.java)
                                    newIntent.putExtra("TransactionType", "Take")

                                    startActivity(newIntent)

                                    overridePendingTransition(com.google.android.material.R.anim.abc_popup_enter, com.google.android.material.R.anim.abc_popup_exit)
                                    finishAfterTransition()
                                }
                            ) {

                                Text(
                                    text = "TAKE",
                                    color = Color(0xFFB71C1C),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W900
                                )

                                /*
                                Icon(
                                    Icons.Filled.Close,
                                    null,
                                    Modifier,
                                    Color.DarkGray
                                )*/
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
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.Center
                    ){

                        Text(
                            modifier = Modifier
                                .padding(0.dp),
                            text = "Spending",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Normal,
                            color = NormalText,
                        )
                        Text(
                            modifier = Modifier
                                .padding(5.dp),
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
                        .requiredHeight(420.dp)
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

                            Column {
                                Text(
                                    modifier = Modifier
                                        .padding(0.dp),
                                    text = "Transactions",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = NormalText,
                                )
                                Text(
                                    modifier = Modifier
                                        .padding(5.dp),
                                    text = "Latest",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.W600,
                                    color = NormalText,
                                )
                            }

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

                                val transactions = data.userTransactionHistory!!.takeLast(5).reversed()

                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    userScrollEnabled = true
                                ){
                                    items(transactions.size){index ->

                                        val hash = transactions.get(index)
                                        val hashData = hash.values.elementAt(0)

                                        var currentTransactionType = TransactionType()

                                        transactionsIndex.forEach{type ->
                                            if(type.name == hashData.transactionType!!){
                                                currentTransactionType = type
                                            }
                                        }

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .wrapContentHeight()
                                                .padding(5.dp)
                                                .background(Color.White, RoundedCornerShape(12.dp)),
                                            horizontalArrangement = Arrangement.SpaceEvenly,
                                            verticalAlignment = Alignment.CenterVertically
                                        ){
                                            Column(
                                                Modifier.padding(5.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ){
                                                Text(
                                                    modifier = Modifier
                                                        .padding(0.dp)
                                                        .background(
                                                            currentTransactionType.textBackgroundColor,
                                                            RoundedCornerShape(12.dp)
                                                        ),
                                                    text = currentTransactionType.categoryIconString,
                                                    fontSize = 60.sp,
                                                    fontWeight = FontWeight.Light,
                                                    color = Color.LightGray,
                                                )
                                                Text(
                                                    text = hashData.transactionType.toString(),
                                                    modifier = Modifier,
                                                    color = currentTransactionType.backgroundColor,
                                                    fontSize = 20.sp,
                                                    fontWeight = FontWeight.W900
                                                )
                                            }

                                            Column(
                                                Modifier.padding(5.dp)
                                            ){
                                                if (hashData.transactionValue!! <= 0){
                                                    Text(
                                                        text = hashData.transactionValue.toString() + "€",
                                                        modifier = Modifier,
                                                        color = Color.Red,
                                                        fontSize = 25.sp,
                                                        fontWeight = FontWeight.W900
                                                    )
                                                }
                                                else{
                                                    Text(
                                                        text = "+"+hashData.transactionValue.toString() + "€",
                                                        modifier = Modifier,
                                                        color = AcceptButtonBG,
                                                        fontSize = 25.sp,
                                                        fontWeight = FontWeight.W900
                                                    )
                                                }

                                                Text(
                                                    text = hashData.transactionName!!.toString(),
                                                    modifier = Modifier,
                                                    color = Color.DarkGray,
                                                    fontSize = 25.sp,
                                                    fontWeight = FontWeight.W900
                                                )


                                                val formatter = SimpleDateFormat("dd.MM.yyyy hh:mm")
                                                val dateString = formatter.format(Date(hashData.transactionDate!!.toLong()))

                                                Text(
                                                    text = dateString.toString(),
                                                    modifier = Modifier,
                                                    color = Color.LightGray,
                                                    fontSize = 20.sp,
                                                    fontWeight = FontWeight.W300
                                                )
                                            }
                                        }
                                    }
                                }
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