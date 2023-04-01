package com.example.zavrsnirad

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
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
            finishAffinity()
            startActivity(Intent(this@BalanceChange, HomeScreen::class.java))
            overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
        }

        setContent{
            val systemUiController = rememberSystemUiController()
            systemUiController.setStatusBarColor(
                color = Color.White
            )

            Scaffold(
                topBar = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(.9f)
                            .requiredHeight(130.dp)
                            .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = {
                                startActivity(Intent(this@BalanceChange, HomeScreen::class.java))
                                finishAfterTransition()
                            }
                        ){
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null,
                                tint = Color.DarkGray,
                                modifier = Modifier.size(48.dp)
                            )
                        }

                        Text(
                            modifier = Modifier
                                .padding(horizontal = 20.dp),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = latoFontFamily,
                            color = NormalText,
                            text = "NEW TRANSACTION"
                        )
                    }
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
                .fillMaxSize()
                .padding(5.dp),
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
                            text = "DESCRIPTION",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = latoFontFamily,
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
                            text = "DATE (${mDate.value})",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = latoFontFamily,
                            color = NormalText,
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ){
                        Spacer(Modifier.width(20.dp))
                        OutlinedButton(
                            border = BorderStroke(2.dp, Color.Magenta),
                            onClick = {
                                mDate.value = SimpleDateFormat("dd/MM/yyyy").format(Date())
                                //Toast.makeText(this@BalanceChange, Date().toString(), Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                modifier = Modifier,
                                text = "TODAY",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Magenta,
                            )
                        }

                        OutlinedButton(
                            border = BorderStroke(2.dp, Color.Red),
                            onClick = {
                                mDatePickerDialog.show()
                            },
                            shape = RoundedCornerShape(20.dp)
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
                    .wrapContentHeight()
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
                            .padding(vertical = 15.dp)
                    ){
                        Text(
                            modifier = Modifier,
                            text = "CATEGORY (${category.value})",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = latoFontFamily,
                            color = NormalText,
                        )
                    }

                    LazyRow(
                        modifier = Modifier.padding(5.dp)
                    ){
                        items(transactionList.size){ index ->
                                Button(
                                    modifier = Modifier
                                        .requiredHeight(150.dp)
                                        .requiredWidth(150.dp)
                                        .padding(horizontal = 5.dp),
                                    shape = RoundedCornerShape(24.dp),
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
                                            fontSize = 65.sp,
                                            fontWeight = FontWeight.Light,
                                            color = Color.LightGray,
                                        )

                                        var categoryCardTextSize = 12.sp

                                        if (transactionList[index].name.length < 9){
                                            categoryCardTextSize = 17.sp
                                        }
                                        else{
                                            categoryCardTextSize = 12.sp
                                        }

                                        Text(
                                            modifier = Modifier,
                                            text = transactionList[index].name,
                                            fontSize = categoryCardTextSize,
                                            fontWeight = FontWeight.Black,
                                            color = Color.White,
                                            textAlign = TextAlign.Center
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
                            text = "AMOUNT",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = latoFontFamily,
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .background(Color.White)
                    .requiredHeight(120.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    modifier = Modifier.size(height = 55.dp, width = 200.dp),
                    border = BorderStroke(3.dp, Color(0xB304AA6D)),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF04AA6D)
                    ),
                    onClick = {
                        ChangeBalance(newAmount, category, transName, mDate, data)
                    }
                ) {
                    Text(
                        text = "CREATE",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontFamily = latoFontFamily,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }

    private fun ChangeBalance(
        newAmount: TextFieldValue,
        category: MutableState<String>,
        transName: TextFieldValue,
        mDate: MutableState<String>,
        data: UserModel
    ) {
        var checkAmount = newAmount.text.toDoubleOrNull()
        if (checkAmount != null && category.value != "" && transName.text != null){
            if (IsAdd(category.value))
                checkAmount = Math.abs(checkAmount)
            else
                checkAmount = -Math.abs(checkAmount)

            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val date = LocalDate.parse(mDate.value, formatter)
            val transaction = TransactionModel(
                transactionName = transName.text,
                transactionType = category.value,
                transactionValue = checkAmount,
                transactionDate = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli().toString(),
                balanceAfterTransaction = data.userBalance!! + checkAmount
            )
            val auth = Firebase.auth
            val user = auth.currentUser
            val database = Firebase.database("https://zavrsnirad-1e613-default-rtdb.europe-west1.firebasedatabase.app/")
            val dbRef = database.getReference("Users")
            dbRef.get().addOnSuccessListener {temp ->

                val userTransactionHistorySnapshot = temp.child(user!!.uid).child("userTransactionHistory")
                val list = mutableListOf<Int>()
                var key = 0

                for (transSnapshot in userTransactionHistorySnapshot.children){
                    val transKey = transSnapshot.key
                    transKey?.let {
                        list.add(it.toInt())
                    }
                }

                list.maxOrNull()?.let{
                    key = it + 1
                    Log.d("****DEBUG****", key.toString())
                }

                val currentUserModel = UserModel()
                currentUserModel.userName = temp.child("userName").getValue(String::class.java)
                currentUserModel.userGender = temp.child("userGender").getValue(String::class.java)
                currentUserModel.userJoin = temp.child("userName").getValue(String::class.java)
                currentUserModel.userId = temp.child("userName").getValue(String::class.java)
                currentUserModel.userBalance = temp.child("userBalance").getValue(Double::class.java)

                val listA = mutableListOf<LinkedHashMap<String, TransactionModel>>()
                for (ds in temp.child("userTransactionHistory").children) {
                    val snapshotKey = ds.key!!
                    val transModel: TransactionModel? = ds.getValue(TransactionModel::class.java)
                    if (transModel != null) {
                        listA.add(linkedMapOf(Pair(snapshotKey, transModel)))
                    }
                }
                currentUserModel.userTransactionHistory = listA

                dbRef.child(user.uid).child("userTransactionHistory").child(key.toString()).setValue(transaction)
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
    }

    private fun findMax(list: List<Int>): Int? {
        return list.max()
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