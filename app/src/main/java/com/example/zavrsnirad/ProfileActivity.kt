package com.example.zavrsnirad

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.R
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zavrsnirad.sealed.DataState
import com.example.zavrsnirad.ui.theme.BGGray
import com.example.zavrsnirad.viewmodels.HomeViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this ) {
            startActivity(Intent(this@ProfileActivity, HomeScreen::class.java))
            overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
            finishAfterTransition()
        }

        setContent {
            val systemUiController = rememberSystemUiController()

            systemUiController.setStatusBarColor(
                color = BGGray
            )

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = BGGray
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp)
                        .background(Color.White, RoundedCornerShape(20.dp)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .requiredHeight(100.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        IconButton(
                            onClick = {
                                startActivity(Intent(this@ProfileActivity, HomeScreen::class.java))
                                finish()
                            }
                        ){
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null,
                                tint = Color.DarkGray,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = "PROFILE",
                            color = Color.DarkGray,
                            fontFamily = latoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 36.sp
                        )
                    }


                    SetData(viewModel)
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
        var emailtxt by remember { mutableStateOf(TextFieldValue("")) }
        var usernametxt by remember { mutableStateOf(TextFieldValue("")) }
        var gendertxt by remember { mutableStateOf("") }
        var passwordtxt by remember { mutableStateOf("") }

        val mAuth = Firebase.auth
        val user = mAuth.currentUser

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.9f)
                .padding(15.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ){
            Card(
                backgroundColor = Color.White,
                elevation = 1.dp,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .requiredHeight(90.dp)
                    .fillMaxWidth()
                    .clickable {
                        val alterProfileIntent = Intent(this@ProfileActivity, AlterProfileActivity::class.java)
                        alterProfileIntent.putExtra("Selection", "Name")
                        startActivity(alterProfileIntent)
                    }
            ){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ){
                        Text(
                            text = "Name:",
                            color = Color.DarkGray,
                            fontFamily = latoFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 32.sp
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ){
                        Text(
                            text = data.userName!!,
                            color = Color.DarkGray,
                            fontFamily = latoFontFamily,
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp
                        )
                    }

                }
            }

            Card(
                backgroundColor = Color.White,
                elevation = 1.dp,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .requiredHeight(90.dp)
                    .fillMaxWidth()
            ){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ){
                        Text(
                            text = "Gender:",
                            color = Color.DarkGray,
                            fontFamily = latoFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 32.sp
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ){
                        Text(
                            text = data.userGender!!,
                            color = Color.DarkGray,
                            fontFamily = latoFontFamily,
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp
                        )
                    }

                }
            }

            Card(
                backgroundColor = Color.White,
                elevation = 1.dp,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .requiredHeight(90.dp)
                    .fillMaxWidth()
            ){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ){
                        Text(
                            text = "Email:",
                            color = Color.DarkGray,
                            fontFamily = latoFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 32.sp
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ){
                        Text(
                            text = user!!.email!!,
                            color = Color.DarkGray,
                            fontFamily = latoFontFamily,
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp
                        )
                    }

                }
            }


            Card(
                backgroundColor = Color.White,
                elevation = 1.dp,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .requiredHeight(90.dp)
                    .fillMaxWidth()
            ){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ){
                        Text(
                            text = "Password:",
                            color = Color.DarkGray,
                            fontFamily = latoFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 32.sp
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ){
                        Text(
                            text = "*************",
                            color = Color.DarkGray,
                            fontFamily = latoFontFamily,
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp
                        )
                    }

                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                OutlinedButton(
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        val intent = Intent(this@ProfileActivity, SplashScreen::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
                        finish()
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = Color(0xFFE94242),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("LOGOUT", color = Color.White, fontWeight = FontWeight.Black, fontSize = 24.sp)
                }
            }
        }
    }
}
