package com.example.zavrsnirad

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zavrsnirad.composables.PieChart
import com.example.zavrsnirad.sealed.DataState
import com.example.zavrsnirad.ui.theme.AcceptButtonBG
import com.example.zavrsnirad.ui.theme.BGGray
import com.example.zavrsnirad.ui.theme.NormalText
import com.example.zavrsnirad.viewmodels.HomeViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.patrykandpatrick.vico.core.entry.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs


@OptIn(ExperimentalAnimationApi::class)
@Suppress("OverrideDeprecatedMigration")
class HomeScreen : ComponentActivity() {
    private lateinit var mAuth: FirebaseAuth
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = Firebase.auth

        setContent{
            val systemUiController = rememberSystemUiController()
            systemUiController.setStatusBarColor(
                color = BGGray
            )
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
                        verticalArrangement = Arrangement.Center
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
                    CircularProgressIndicator(color = Color.DarkGray)
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

    @SuppressLint("SimpleDateFormat")
    @Composable
    fun ShowColumn(data: UserModel) {
        var hasSpendingRecordLastMonth: Boolean = false

        val checkTransactions = data.userTransactionHistory!!.takeLast(10).reversed()
        checkTransactions.forEach{ model ->
            val checkHash = model.values.elementAt(0)
            val checkFormatter = SimpleDateFormat("dd.MM.yyyy")

            val transDate = Date(checkHash.transactionDate!!.toLong())
            val transDateString = checkFormatter.format(transDate).toString()

            val transDateParsed = checkFormatter.parse(transDateString)
            val transYear = SimpleDateFormat("yyyy").format(transDateParsed!!).toInt()
            val transMonth = SimpleDateFormat("MM").format(transDateParsed!!).toInt()

            val todayYear = SimpleDateFormat("yyyy").format(Date().time).toInt()
            val todayMonth = SimpleDateFormat("MM").format(Date().time).toInt()

            if ((todayYear == transYear) && (todayMonth == transMonth) && (!IsAdd(checkHash.transactionType!!))){
                Log.d("****DEBUG****", "Found match for transaction last month!")
                hasSpendingRecordLastMonth = true;
                return@forEach
            }
        }


        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(60.dp)
                    .padding(5.dp),
                horizontalArrangement = Arrangement.End
            ){
                IconButton(
                    onClick = {
                        val newIntent = Intent(this@HomeScreen, ProfileActivity::class.java)
                        finishAffinity()
                        startActivity(newIntent)
                    }) {
                    Icon(
                        Icons.Filled.AccountCircle,
                        null,
                        Modifier.size(42.dp),
                        Color.DarkGray
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(180.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = buildAnnotatedString {
                        Column(
                            modifier = Modifier,
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Text(
                                text="Welcome back!",
                                modifier = Modifier
                                    .padding(horizontal = 15.dp, vertical = 0.dp),
                                fontWeight = FontWeight.Normal,
                                fontSize = 42.sp,
                                color = NormalText,
                                textAlign = TextAlign.Left,
                            )
                            Text(
                                text = "${data.userName}",
                                modifier = Modifier
                                    .padding(horizontal = 15.dp, vertical = 0.dp),
                                fontWeight = FontWeight.Bold,
                                fontSize = 38.sp,
                                color = NormalText,
                                textAlign = TextAlign.Left,
                            )
                        }
                    },
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
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
                            fontWeight = FontWeight.Normal,
                            fontSize = 32.sp,
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
                                fontSize = 62.sp,
                                fontWeight = FontWeight.Black,
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
                                border = BorderStroke(2.dp, Color.Blue),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = Color.DarkGray),
                                onClick = {
                                    val newIntent = Intent(this@HomeScreen, BalanceChange::class.java)
                                    newIntent.putExtra("TransactionType", "Add")
                                    startActivity(newIntent)
                                    overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                                    finishAfterTransition()
                                }
                            ) {

                                Text(
                                    text = "ADD",
                                    color = Color.Blue,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W900
                                )
                            }

                            Spacer(Modifier.width(5.dp))

                            OutlinedButton(
                                modifier = Modifier.size(height= 40.dp, width = 90.dp),
                                border = BorderStroke(2.dp, Color.DarkGray),
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
                                    color = Color.DarkGray,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W900
                                )
                            }
                        }
                    }
                }
            }

            // Spending card and row
            if (hasSpendingRecordLastMonth){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
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
                            ) {
                                val transactionsData = data.userTransactionHistory!!
                                val transactionsLastMonth = mutableListOf<TransactionModel>()

                                for (transaction in transactionsData) {
                                    val transactionHash = transaction.values.elementAt(0)
                                    val transactionDate = transactionHash.transactionDate!!.toLong()
                                    val dateFormat = Date(transactionDate)
                                    val today = Date().time

                                    val todayYear = SimpleDateFormat("yyyy").format(today).toInt()
                                    val todayMonth = SimpleDateFormat("MM").format(today).toInt()
                                    val todayDay = SimpleDateFormat("dd").format(today).toInt()
                                    val todayDayInYear = SimpleDateFormat("DDD").format(today).toInt()

                                    val transactionYear = SimpleDateFormat("yyyy").format(dateFormat).toInt()
                                    val transactionMonth = SimpleDateFormat("MM").format(dateFormat).toInt()
                                    val transactionDay = SimpleDateFormat("dd").format(dateFormat).toInt()
                                    val transactionDayInYear = SimpleDateFormat("DDD").format(dateFormat).toInt()

                                    if (transactionYear == todayYear && transactionMonth == todayMonth) {
                                        transactionsLastMonth.add(transaction.values.elementAt(0))
                                        //Log.d("****DEBUG****", "Transaction -->" + transaction.values.elementAt(0).toString())
                                    }
                                }

                                val groupedTransactions = transactionsLastMonth
                                    .filter { (it.transactionValue ?: 0.0) < 0 }
                                    .groupBy(TransactionModel::transactionType)

                                val data = groupedTransactions
                                    .mapKeys { it.key!! }
                                    .mapValues { entry -> abs(entry.value.sumOf { it.transactionValue ?: 0.0 }).toInt() }

                                //Log.d("****DEBUG****", data.toString())
                                PieChart(
                                    data = data
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

                var height = 300.dp

                if (data.userTransactionHistory!!.takeLast(3).reversed().size >= 2){
                    height = 420.dp
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(height)
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
                                onClick = {
                                    startActivity(Intent(this@HomeScreen, AllTransactions::class.java))
                                    finishAffinity()
                                }
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
                                    .background(color = BGGray, RoundedCornerShape(20.dp)),
                            ){

                                val transactions = data.userTransactionHistory!!.takeLast(5).reversed()

                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    userScrollEnabled = true
                                ){
                                    items(transactions.size){index ->

                                        val hash = transactions[index]
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
                                                .background(Color.White, RoundedCornerShape(25.dp)),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceEvenly
                                        ){
                                            Column(
                                                Modifier.padding(5.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally,
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
                                                    fontWeight = FontWeight.W900,
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

                                                val formatter = SimpleDateFormat("dd.MM.yyyy")
                                                val date = Date(hashData.transactionDate!!.toLong())
                                                val dateString = formatter.format(date)

                                                val year = SimpleDateFormat("yyyy").format(date)
                                                val month = SimpleDateFormat("MM").format(date)
                                                val day = SimpleDateFormat("dd").format(date)

                                                Text(
                                                    text = dateString.toString(),
                                                    modifier = Modifier,
                                                    color = Color.DarkGray,
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.W600
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){

            }
        }
    }

    private fun IsAdd(value: String): Boolean {
        var isIt = false
        depositTransactionCategories.forEach{ type ->
            if(type.name == value){
                isIt = true
            }
        }
        return isIt
    }
}