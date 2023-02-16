package com.example.zavrsnirad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.MoneyOffCsred
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

class BalanceChange : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this ) {
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
                                    .size(140.dp)
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
        var category by remember {mutableStateOf("")}

        Column(
            modifier = Modifier
                .fillMaxSize(),
        ){
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(180.dp)
                    .padding(10.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = 2.dp,
                backgroundColor = Color.White
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        modifier = Modifier
                            .padding(10.dp),
                        text = "Current balance",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Normal,
                        color = NormalText,
                    )

                    Text(
                        modifier = Modifier
                            .padding(10.dp),
                        text = data.userBalance.toString() + "€",
                        fontSize = 45.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray,
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(180.dp)
                    .padding(10.dp),
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
                            text = "Category",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Normal,
                            color = NormalText,
                        )
                    }
                    Row(

                    ){

                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(180.dp)
                    .padding(10.dp),
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
                            text = "Price",
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
                            fontSize = 30.sp,
                            fontWeight = FontWeight.W900,
                        ),
                        placeholder = {
                            Text(
                                modifier = Modifier,
                                text = "0.00" + "€",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Light,
                                color = Color.LightGray,
                            )
                        }



                    )
                }
            }
        }
    }
}