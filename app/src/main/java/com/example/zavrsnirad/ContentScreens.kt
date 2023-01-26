package com.example.zavrsnirad

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.RoundedCorner
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zavrsnirad.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


@Composable
fun BalanceScreen(){
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = BGGray
    ){
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ){

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth().size(160.dp)
                ){
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 20.dp),
                        text = "Welcome back, Dorijan!",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Normal,
                        color = NormalText,
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
                        .requiredHeight(225.dp)
                        .padding(10.dp),
                    shape = RoundedCornerShape(10.dp),
                    elevation = 2.dp,
                    backgroundColor = White
                ){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ){

                        Text(
                            modifier = Modifier
                                .padding(10.dp),
                            text = "Your balance",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Normal,
                            color = NormalText,
                        )

                        Text(
                            modifier = Modifier
                                .padding(10.dp),
                            text = "$200.00",
                            fontSize = 45.sp,
                            fontWeight = FontWeight.Medium,
                            color = Black,
                        )
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
                        .requiredHeight(850.dp)
                        .padding(10.dp),
                    shape = RoundedCornerShape(10.dp),
                    elevation = 2.dp,
                    backgroundColor = White
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
                                Pair("String-1", 100),
                                Pair("String-2", 150),
                                Pair("String-3", 210),
                                Pair("String-4", 430),
                                Pair("String-5", 40),
                            )
                        )
                    }
                }
            }
        }
    }

    /*
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .wrapContentSize(Alignment.Center)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .verticalScroll(rememberScrollState())
                .padding(10.dp)
        ){
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ){

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(250.dp)
                        .padding(10.dp)
                        .verticalScroll(rememberScrollState()),
                    backgroundColor = Color.hsl(0f, 0f, 0.98f, 1f),
                    shape = MaterialTheme.shapes.large,
                    elevation = 0.dp
                ){
                    Text(
                        text = "BALANCE",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.W900,
                        color = Color.hsl(0f, 0.00f, 0.25f),
                        modifier = Modifier
                            .padding(10.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Transparent)
                            .wrapContentSize(Alignment.Center),

                    ){
                        Text(
                            text = "$30.00",
                            fontSize = 70.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.hsl(0f, 0.00f, 0.45f),
                            modifier = Modifier
                                .padding(20.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(750.dp)
                        .padding(10.dp)
                        .verticalScroll(rememberScrollState()),
                    backgroundColor = Color.hsl(0f, 0f, 0.98f, 1f),
                    shape = MaterialTheme.shapes.large,
                    elevation = 0.dp
                ){
                    Text(
                        text = "BALANCE",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.W900,
                        color = Color.hsl(0f, 0.00f, 0.25f),
                        modifier = Modifier
                            .padding(10.dp)
                    )

                    Spacer(modifier = Modifier.height(50.dp))

                    PieChart(
                        data = mapOf(
                            Pair("Kredit", 700),
                            Pair("String-2", 120),
                            Pair("String-3", 300),
                            Pair("String-4", 200),
                            Pair("String-5", 110)
                        ),
                        radiusOuter = 90.dp,
                        chartBarWidth = 30.dp
                    )

                }
            }
        }
    }
    */
}

@Preview(showBackground = true)
@Composable
fun BalanceScreenPreview(){
    BalanceScreen()
}


@Composable
fun DeptsScreen(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .wrapContentSize(Alignment.Center)
    ) {

    }
}

@Preview(showBackground = true)
@Composable
fun DeptsScreenPreview(){
    DeptsScreen()
}

@Composable
fun ProfileScreen(){
    val context = (LocalContext.current as? Activity)

    var emailtxt by remember { mutableStateOf(TextFieldValue("")) }
    var usernametxt by remember { mutableStateOf(TextFieldValue("")) }
    var gendertxt by remember {mutableStateOf("")}
    var passwordtxt by remember {mutableStateOf("")}

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ){
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(
                    horizontal = 40.dp,
                    vertical = 20.dp
                )
                .fillMaxSize()
        ){

                Text(text = "Profile", color = Color.DarkGray, fontSize = 40.sp)

                Spacer(Modifier.height(50.dp))

                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                ){
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(text = "NAME", color = Color.DarkGray, fontSize = 20.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Start, modifier = Modifier.weight(1f))
                            IconButton(onClick = {}){
                                Icon(Icons.Filled.ArrowForwardIos, null, tint = Color.DarkGray, modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(Modifier.height(5.dp))
                        if (usernametxt.text != "")
                            Text(text = usernametxt.text, color = Color.DarkGray, fontSize = 18.sp, fontWeight = FontWeight.Normal)
                        else
                            Text(text = "/", color = Color.DarkGray, fontSize = 18.sp, fontWeight = FontWeight.Normal)
                    }

                    Spacer(Modifier.height(35.dp))

                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(text = "GENDER", color = Color.DarkGray, fontSize = 20.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Start, modifier = Modifier.weight(1f))
                            IconButton(onClick = {}){
                                Icon(Icons.Filled.ArrowForwardIos, null, tint = Color.DarkGray, modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(Modifier.height(5.dp))
                        if (gendertxt != "")
                            Text(text = gendertxt, color = Color.DarkGray, fontSize = 18.sp, fontWeight = FontWeight.Normal)
                        else
                            Text(text = "/", color = Color.DarkGray, fontSize = 18.sp, fontWeight = FontWeight.Normal)
                    }

                    Spacer(Modifier.height(35.dp))

                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(text = "EMAIL", color = Color.DarkGray, fontSize = 20.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Start, modifier = Modifier.weight(1f))
                            IconButton(onClick = {}){
                                Icon(Icons.Filled.ArrowForwardIos, null, tint = Color.DarkGray, modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(Modifier.height(5.dp))
                        if (emailtxt.text != "")
                            Text(text = emailtxt.text, color = Color.DarkGray, fontSize = 18.sp, fontWeight = FontWeight.Normal)
                        else
                            Text(text = "/", color = Color.DarkGray, fontSize = 18.sp, fontWeight = FontWeight.Normal)
                    }

                    Spacer(Modifier.height(35.dp))

                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(text = "PASSWORD", color = Color.DarkGray, fontSize = 20.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Start, modifier = Modifier.weight(1f))
                            IconButton(onClick = {}){
                                Icon(Icons.Filled.ArrowForwardIos, null, tint = Color.DarkGray, modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(Modifier.height(5.dp))
                        Text(text = "*********", color = Color.DarkGray, fontSize = 18.sp, fontWeight = FontWeight.Normal)
                    }

                    Spacer(Modifier.height(45.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ){
                        OutlinedButton(
                            onClick = {
                                FirebaseAuth.getInstance().signOut()
                                var intent = Intent(context, SplashScreen::class.java)
                                context?.startActivity(intent)
                                context?.overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                                context?.finish()
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                backgroundColor = Color.hsl(5f, 0.00f, 0.20f, 1f),
                                contentColor = Color.White
                            ),
                        ) {
                            Text("LOG OUT", color = Color.White, fontWeight = FontWeight.Black, fontSize = 24.sp)
                        }
                    }

                }


                /*
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){


                    TextField(
                        value = usernametxt,
                        leadingIcon = {Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "emailIcon")
                        },
                        onValueChange = {
                            usernametxt = it
                        },
                        singleLine = true,
                        label = { Text(text = "Username") },
                        placeholder = { Text(text = "Enter your username") },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,

                        )
                    )

                    OutlinedTextField(
                        value = usernametxt,
                        leadingIcon = {Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "emailIcon")
                        },
                        onValueChange = {
                            usernametxt = it
                        },
                        singleLine = true,
                        label = { Text(text = "Username") },
                        placeholder = { Text(text = "Enter your username") }
                    )

                    // EMAIL TEXT BOX
                    OutlinedTextField(
                        value = emailtxt,
                        leadingIcon = {Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "emailIcon")
                        },
                        onValueChange = {
                            emailtxt = it
                        },
                        singleLine = true,
                        label = { Text(text = "Email address") },
                        placeholder = { Text(text = "Enter your e-mail") }
                    )

                }*/

            }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview(){
    ProfileScreen()
}
