package com.example.zavrsnirad

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.RoundedCorner
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.DarkGray
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
import androidx.lifecycle.ViewModel
import com.example.zavrsnirad.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

@Composable
fun BalanceScreen(){

    val mAuth = Firebase.auth
    val mDatabase = Firebase.database("https://zavrsnirad-1e613-default-rtdb.europe-west1.firebasedatabase.app/")
    val mRef = mDatabase.getReference("Users")
    val user = mAuth.currentUser
    var userData = UserModel()

    mDatabase.getReference("Users").child(user!!.uid).get().addOnSuccessListener {ds ->

        Log.w("Firebase", "Adding userName to the object")
        userData.userName = ds.child("userName").value.toString()
        userData.userBalance = ds.child("userName").value.toString().toDoubleOrNull()
        userData.userId = user.uid
        userData.userGender = ds.child("userGender").value.toString()

        //Log.i("Firebase", "User name is : ${userData.userName} ")
    }.addOnFailureListener{
        Log.e("Firebase", "Error getting data", it)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
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
            ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(160.dp)
                ){
                    Log.w("Firebase", "${userData.userName}, ${userData.userGender}")
                    Log.w("Firebase", "String for username loaded")

                    Text(
                        modifier = Modifier
                            .padding(horizontal = 20.dp),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Normal,
                        color = NormalText,
                        text = "Welcome back! ${userData.userName}"
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
                            text = ((Math.random()*10000.0).roundToInt() / 100.0).toString() + "€",
                            fontSize = 45.sp,
                            fontWeight = FontWeight.Medium,
                            color = Black,
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ){
                            OutlinedButton(
                                onClick = {},
                                modifier = Modifier.padding(5.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    backgroundColor = BGGray,
                                    contentColor = White,
                                ),
                                border = BorderStroke(0.dp, White)
                            ) {
                                Text("MODIFY", color = DarkGray, fontWeight = FontWeight.W900, fontSize = 17.sp)
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
            .background(White)
            .wrapContentSize(Alignment.Center)
    ) {

    }
}

@Preview(showBackground = true)
@Composable
fun DeptsScreenPreview(){
    DeptsScreen()
}

