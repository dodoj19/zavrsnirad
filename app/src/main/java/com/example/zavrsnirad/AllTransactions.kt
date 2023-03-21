package com.example.zavrsnirad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zavrsnirad.sealed.DataState
import com.example.zavrsnirad.ui.theme.BGGray
import com.example.zavrsnirad.viewmodels.HomeViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.text.SimpleDateFormat
import java.util.*

class AllTransactions : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()
            systemUiController.setStatusBarColor(
                color = BGGray
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BGGray)
                    .padding(25.dp),
            ){
                LoadData(viewModel)
            }
        }
    }

    @Composable
    fun LoadData(viewModel: HomeViewModel) {
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
                LazyTransactions(result.data)
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
    private fun LazyTransactions(data: UserModel) {
        val transactions = data.userTransactionHistory!!.takeLast(100).reversed()

        Row(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ){
            IconButton(
                onClick = {
                    finish()
                }
            ){
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.DarkGray,
                    modifier = Modifier.size(42.dp)
                )
            }

            Spacer(Modifier.width(10.dp))

            Text(text = "All transactions", color = Color.DarkGray, fontSize = 38.sp)
        }

        Spacer(Modifier.height(25.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            userScrollEnabled = true
        ){
            items(transactions.size){index ->

                val hash = transactions.get(index)
                val hashData = hash.values.elementAt(0)
                var currentTransactionType = TransactionType()

                val formatter = SimpleDateFormat("dd.MM.yyyy")
                val date = Date(hashData.transactionDate!!.toLong())
                val dateString = formatter.format(date)

                val year = SimpleDateFormat("yyyy").format(date)
                val month = SimpleDateFormat("MM").format(date)
                val day = SimpleDateFormat("dd").format(date)

                transactionsIndex.forEach{type ->
                    if(type.name == hashData.transactionType!!){
                        currentTransactionType = type
                    }
                }

                Card(
                    modifier = Modifier,
                    backgroundColor = Color.White,
                    elevation = 5.dp,
                    shape = RoundedCornerShape(20.dp)
                ){
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Column(
                            modifier = Modifier.padding(5.dp),
                            verticalArrangement = Arrangement.SpaceEvenly,
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
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W900
                            )
                        }

                        Column(
                            modifier = Modifier.padding(5.dp),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
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
                                    color = Color.Green,
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

                Spacer(Modifier.height(15.dp))

                // TODO("Add card with information about the transaction, add icon and ability to alter/delete records.");
            }
        }
    }
}