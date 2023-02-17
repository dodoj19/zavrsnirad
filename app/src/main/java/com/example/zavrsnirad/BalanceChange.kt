package com.example.zavrsnirad

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.zavrsnirad.sealed.DataState
import com.example.zavrsnirad.ui.theme.BGGray
import com.example.zavrsnirad.ui.theme.NormalText
import com.example.zavrsnirad.viewmodels.HomeViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class BalanceChange : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this ) {
            startActivity(Intent(this@BalanceChange, HomeScreen::class.java))
            overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
            finishAfterTransition()
        }

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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .size(120.dp)
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(horizontal = 20.dp),
                                    fontSize = 38.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = NormalText,
                                    text = "New transaction"
                                )
                            }
                        }

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
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator()
                }
            }
            is DataState.Success->{
                ShowColumnData(result.data)
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
    fun ShowColumnData(data: UserModel) {

        var newAmount by remember { mutableStateOf(TextFieldValue("")) }
        var transName by remember {mutableStateOf(TextFieldValue(""))}
        var category by remember {mutableStateOf("")}

        Column(
            modifier = Modifier
                .fillMaxSize(),
        ){
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(160.dp)
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = 2.dp,
                backgroundColor = Color.White
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    verticalArrangement = Arrangement.Center
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Text(
                            modifier = Modifier,
                            text = "Description",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Normal,
                            color = NormalText,
                        )
                    }

                    TextField(
                        value = transName,
                        onValueChange = { newText ->
                            transName = newText
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.DarkGray,
                            unfocusedBorderColor = Color.LightGray,
                            backgroundColor = Color.Transparent,
                            cursorColor = Color.LightGray,
                            textColor = Color.DarkGray,
                            errorBorderColor = Color.Transparent,
                        ),
                        textStyle = TextStyle(
                            color = Color.DarkGray,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Normal,
                        ),
                        placeholder = {
                            Text(
                                modifier = Modifier,
                                text = "Description",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Light,
                                color = Color.LightGray,
                            )
                        }
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(240.dp)
                    .padding(10.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = 2.dp,
                backgroundColor = Color.White
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalArrangement = Arrangement.Center
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                    ){
                        Text(
                            modifier = Modifier,
                            text = "Category",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Normal,
                            color = NormalText,
                        )
                    }
                    LazyRow(
                        modifier = Modifier.padding(5.dp)
                    ){
                        items(transactionCategories.size){ index ->
                                Button(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .requiredWidth(120.dp)
                                        .padding(horizontal = 5.dp),
                                    shape = RoundedCornerShape(20.dp),
                                    elevation = ButtonDefaults.elevation(
                                        defaultElevation = 6.dp,
                                        pressedElevation = 8.dp,
                                        disabledElevation = 0.dp
                                    ),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = transactionCategories[index].backgroundColor,
                                    ),
                                    onClick = {
                                        category = transactionCategories[index].name
                                        Log.d("TEXTST", category)
                                    }
                                ){

                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.SpaceAround,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ){
                                        Text(
                                            modifier = Modifier
                                                .padding(0.dp)
                                                .background(
                                                    transactionCategories[index].textBackgroundColor,
                                                    RoundedCornerShape(12.dp)
                                                ),
                                            text = transactionCategories[index].categoryIconString,
                                            fontSize = 50.sp,
                                            fontWeight = FontWeight.Light,
                                            color = Color.LightGray,
                                        )
                                        Text(
                                            modifier = Modifier,
                                            text = transactionCategories[index].name,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.W900,
                                            color = Color.White,
                                        )
                                    }
                                }
                        }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(160.dp)
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = 2.dp,
                backgroundColor = Color.White
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    verticalArrangement = Arrangement.Center
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Text(
                            modifier = Modifier,
                            text = "Amount",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Normal,
                            color = NormalText,
                        )
                    }

                    TextField(
                        value = newAmount,
                        onValueChange = { newText ->
                            newAmount = newText
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.DarkGray,
                            unfocusedBorderColor = Color.LightGray,
                            backgroundColor = Color.Transparent,
                            cursorColor = Color.LightGray,
                            textColor = Color.DarkGray,
                            errorBorderColor = Color.Transparent,
                        ),
                        textStyle = TextStyle(
                            color = Color.DarkGray,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.W900,
                        ),
                        placeholder = {
                            Text(
                                modifier = Modifier,
                                text = "0.00" + "€",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Light,
                                color = Color.LightGray,
                            )
                        }
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(100.dp)
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = 2.dp,
                backgroundColor = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        modifier = Modifier.size(height = 40.dp, width = 130.dp),
                        border = BorderStroke(2.dp, Color(0xFF5AC537)),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White,
                            contentColor = Color.DarkGray
                        ),
                        onClick = {

                            val checkAmount = newAmount.text.toDoubleOrNull()

                            if (checkAmount != null && category != "" && transName.text != null){
                                val transaction = TransactionModel(
                                    transactionName = transName.text,
                                    transactionType = category,
                                    transactionValue = checkAmount,
                                    transactionDate = Date().time.toString()
                                )

                                val auth = Firebase.auth
                                val user = auth.currentUser
                                val database = Firebase.database("https://zavrsnirad-1e613-default-rtdb.europe-west1.firebasedatabase.app/")
                                val dbRef = database.getReference("Users")

                                dbRef.child(user!!.uid).child("userTransactionHistory").child(Date().time.toString()).setValue(transaction)
                                    .addOnCompleteListener {
                                        val balReference = dbRef.child(user.uid).child("userBalance")
                                        balReference.setValue(data.userBalance!! + checkAmount).addOnCompleteListener {
                                            startActivity(Intent(this@BalanceChange, HomeScreen::class.java))
                                            overridePendingTransition(
                                                com.google.android.material.R.anim.abc_popup_enter,
                                                com.google.android.material.R.anim.abc_popup_exit
                                            )
                                            finishAfterTransition()
                                        }
                                    }
                            }
                        }
                    ) {
                        Text(
                            text = "ACCEPT",
                            color = Color(0xFF5AC537),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W900
                        )
                        Icon(
                            Icons.Filled.Check,
                            null,
                            Modifier,
                            Color(0xFF5AC537)
                        )
                    }

                    Spacer(Modifier.width(20.dp))

                    OutlinedButton(
                        modifier = Modifier.size(height = 40.dp, width = 130.dp),
                        border = BorderStroke(2.dp, Color(0xFFDA3B20)),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White,
                            contentColor = Color.DarkGray
                        ),
                        onClick = {
                            startActivity(Intent(this@BalanceChange, HomeScreen::class.java))
                            overridePendingTransition(
                                com.google.android.material.R.anim.abc_popup_enter,
                                com.google.android.material.R.anim.abc_popup_exit
                            )
                            finishAfterTransition()
                        }
                    ) {
                        Text(
                            text = "CANCEL",
                            color = Color(0xFFDA3B20),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W900
                        )
                        Icon(
                            Icons.Filled.Cancel,
                            null,
                            Modifier,
                            Color(0xFFDA3B20)
                        )
                    }
                }
            }


        }
    }
}