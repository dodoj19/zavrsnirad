package com.example.zavrsnirad

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zavrsnirad.sealed.DataState
import com.example.zavrsnirad.ui.theme.BGGray
import com.example.zavrsnirad.ui.theme.NormalText
import com.example.zavrsnirad.viewmodels.HomeViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class BalanceChange : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()
    private var transactionType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        transactionType = intent.extras!!.getString("TransactionType")!!

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
        val category = remember {mutableStateOf("")}

        var transactionList: List<TransactionType>

        if (transactionType == "Add")
            transactionList = depositTransactionCategories
        else
            transactionList = withdrawTransactionCategories

        // Fetching the Local Context
        val mContext = LocalContext.current

        // Declaring integer values
        // for year, month and day
        val mYear: Int
        val mMonth: Int
        val mDay: Int

        // Initializing a Calendar
        val mCalendar = Calendar.getInstance()

        // Fetching current year, month and day
        mYear = mCalendar.get(Calendar.YEAR)
        mMonth = mCalendar.get(Calendar.MONTH)
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

        mCalendar.time = Date()

        // Declaring a string value to
        // store date in string format
        val mDate = remember { mutableStateOf("") }
        mDate.value = SimpleDateFormat("dd/MM/yyyy").format(Date())

        // Declaring DatePickerDialog and setting
        // initial values as current values (present year, month and day)
        val mDatePickerDialog = DatePickerDialog(
            mContext,
            { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->

                var result: String = ""

                if (mDayOfMonth < 10){
                    result = result + "0" + mDayOfMonth.toString()
                }
                else{
                    result += mDayOfMonth.toString()
                }

                result += "/"

                if (mMonth+1 < 10){
                    result = result + "0" + (mMonth+1).toString()
                }
                else{
                    result += (mMonth+1).toString()
                }

                result += "/"
                result += mYear.toString()

                mDate.value = result

                //mDate.value = "$mDayOfMonth/${mMonth+1}/$mYear"
            }, mYear, mMonth, mDay
        )

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
                    .requiredHeight(160.dp)
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = 2.dp,
                backgroundColor = Color.White
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    verticalArrangement = Arrangement.SpaceAround
                ){
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Text(
                            modifier = Modifier,
                            text = "Date (${mDate.value})",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Normal,
                            color = NormalText,
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ){
                        Spacer(Modifier.width(20.dp))
                        OutlinedButton(
                            border = BorderStroke(1.dp, Color.Blue),
                            onClick = {
                                mDate.value = SimpleDateFormat("dd/MM/yyyy").format(Date())
                                Toast.makeText(this@BalanceChange, Date().toString(), Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Text(
                                modifier = Modifier,
                                text = "TODAY",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Blue,
                            )
                        }

                        OutlinedButton(
                            border = BorderStroke(1.dp, Color.Red),
                            onClick = {
                                mDatePickerDialog.show()
                            }
                        ) {
                            Text(
                                modifier = Modifier,
                                text = "PICK A DATE",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Red,
                            )
                        }
                        Spacer(Modifier.width(20.dp))
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(260.dp)
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
                            text = "Category (${category.value})",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Normal,
                            color = NormalText,
                        )
                    }
                    LazyRow(
                        modifier = Modifier.padding(5.dp)
                    ){
                        items(transactionList.size){ index ->
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
                                        backgroundColor = transactionList[index].backgroundColor,
                                    ),
                                    onClick = {
                                        category.value = transactionList[index].name
                                        Log.d("TEXTST", category.value)
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
                                                    transactionList[index].textBackgroundColor,
                                                    RoundedCornerShape(12.dp)
                                                ),
                                            text = transactionList[index].categoryIconString,
                                            fontSize = 50.sp,
                                            fontWeight = FontWeight.Light,
                                            color = Color.LightGray,
                                        )
                                        Text(
                                            modifier = Modifier,
                                            text = transactionList[index].name,
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

                            var checkAmount = newAmount.text.toDoubleOrNull()

                            if (checkAmount != null && category.value != "" && transName.text != null){

                                if (transactionType == "Add")
                                    checkAmount = Math.abs(checkAmount)
                                else
                                    checkAmount = -Math.abs(checkAmount)

                                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                val date = LocalDate.parse(mDate.value, formatter)

                                Log.d("****DEBUG****", date.toString())
                                Log.d("****DEBUG****", date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli().toString())

                                val transaction = TransactionModel(
                                    transactionName = transName.text,
                                    transactionType = category.value,
                                    transactionValue = checkAmount,
                                    transactionDate = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli().toString()
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