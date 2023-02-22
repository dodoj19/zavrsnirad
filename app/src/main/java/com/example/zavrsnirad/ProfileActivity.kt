package com.example.zavrsnirad

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zavrsnirad.sealed.DataState
import com.example.zavrsnirad.ui.theme.BGGray
import com.example.zavrsnirad.viewmodels.HomeViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()
            systemUiController.setStatusBarColor(
                color = BGGray
            )
            Scaffold(
                topBar = {
                }
            ){paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
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
            ShowProfile(result.data)
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
fun ShowProfile(data: UserModel){
    val context = (LocalContext.current as? Activity)

    var emailtxt by remember { mutableStateOf(TextFieldValue("")) }
    var usernametxt by remember { mutableStateOf(TextFieldValue("")) }
    var gendertxt by remember { mutableStateOf("") }
    var passwordtxt by remember { mutableStateOf("") }

    val mAuth = Firebase.auth
    val user = mAuth.currentUser

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BGGray),
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

            Row(
                Modifier
                    .fillMaxWidth()
                    .background(BGGray),
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(
                    onClick = {
                        context?.overridePendingTransition(com.google.android.material.R.anim.abc_fade_in, com.google.android.material.R.anim.abc_fade_out)
                        context?.finishAfterTransition()
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

                Text(text = "Profile", color = Color.DarkGray, fontSize = 42.sp)
            }

            Spacer(Modifier.height(35.dp))

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
                        Text(text = "NAME", color = Color.DarkGray, fontSize = 25.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Start, modifier = Modifier.weight(1f))
                        IconButton(onClick = {}){
                            Icon(Icons.Filled.ArrowForwardIos, null, tint = Color.DarkGray, modifier = Modifier.size(20.dp))
                        }
                    }
                    Spacer(Modifier.height(5.dp))
                    if (usernametxt.text != "")
                        Text(text = usernametxt.text, color = Color.DarkGray, fontSize = 18.sp, fontWeight = FontWeight.Normal)
                    else
                        Text(text = user!!.email!!.split("@")[0], color = Color.DarkGray, fontSize = 16.sp, fontWeight = FontWeight.Normal)
                }

                Spacer(Modifier.height(35.dp))

                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(text = "GENDER", color = Color.DarkGray, fontSize = 25.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Start, modifier = Modifier.weight(1f))
                        IconButton(onClick = {}){
                            Icon(Icons.Filled.ArrowForwardIos, null, tint = Color.DarkGray, modifier = Modifier.size(20.dp))
                        }
                    }
                    Spacer(Modifier.height(5.dp))
                    if (gendertxt != "")
                        Text(text = gendertxt, color = Color.DarkGray, fontSize = 18.sp, fontWeight = FontWeight.Normal)
                    else
                        Text(text = "Undefined", color = Color.DarkGray, fontSize = 16.sp, fontWeight = FontWeight.Normal)
                }

                Spacer(Modifier.height(35.dp))

                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(text = "EMAIL", color = Color.DarkGray, fontSize = 25.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Start, modifier = Modifier.weight(1f))
                        IconButton(onClick = {}){
                            Icon(Icons.Filled.ArrowForwardIos, null, tint = Color.DarkGray, modifier = Modifier.size(20.dp))
                        }
                    }
                    Spacer(Modifier.height(5.dp))
                    if (emailtxt.text != "")
                        Text(text = emailtxt.text, color = Color.DarkGray, fontSize = 18.sp, fontWeight = FontWeight.Normal)
                    else
                        Text(text = user!!.email!!.toString(), color = Color.DarkGray, fontSize = 16.sp, fontWeight = FontWeight.Normal)
                }

                Spacer(Modifier.height(35.dp))

                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(text = "PASSWORD", color = Color.DarkGray, fontSize = 25.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Start, modifier = Modifier.weight(1f))
                        IconButton(onClick = {}){
                            Icon(Icons.Filled.ArrowForwardIos, null, tint = Color.DarkGray, modifier = Modifier.size(20.dp))
                        }
                    }
                    Spacer(Modifier.height(5.dp))
                    Text(text = "*********", color = Color.DarkGray, fontSize = 20.sp, fontWeight = FontWeight.Normal)
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
                            context?.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
                            context?.finish()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            backgroundColor = Color(0xFFD64A4A),
                            contentColor = Color.White
                        ),
                    ) {
                        Text("LOGOUT", color = Color.White, fontWeight = FontWeight.Black, fontSize = 24.sp)
                    }
                }
            }
        }
    }
}
