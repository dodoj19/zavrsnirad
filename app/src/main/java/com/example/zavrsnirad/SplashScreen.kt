package com.example.zavrsnirad

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zavrsnirad.ui.theme.Zavrsniradtest1Theme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashScreen : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        //auth.signOut()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser == null){
            setContent{
                Zavrsniradtest1Theme {
                    DefaultPreview()
                }
            }
        }
        else{
            startActivity(Intent(this@SplashScreen, HomeScreen::class.java))
            overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
            finishAfterTransition()
        }
    }

    @Composable
    fun MainScreen(){
        val systemUiController = rememberSystemUiController()
        systemUiController.setSystemBarsColor(
            color = Color.White
        )
        systemUiController.setStatusBarColor(
            color = Color.White
        )

        Scaffold(
            topBar = {},
            bottomBar = {Text(text = getString(R.string.app_version), color = Color.LightGray, fontSize = 12.sp)},
            content = {
                Surface(
                    color = Color.White,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .requiredWidthIn(min = 250.dp, max = 300.dp),
                        verticalArrangement = Arrangement.Center
                    ){

                        Image(painter = painterResource(id = R.drawable.splash_logo), contentDescription = "Logo")

                        Text(
                            text = "Wallet",
                            color = Color.DarkGray,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier,
                            fontSize = 45.sp,
                            fontFamily = FontFamily.SansSerif
                        )
                        Text(
                            text = "Where saving begins.",
                            color = Color.LightGray,
                            modifier = Modifier,
                            fontSize = 19.sp,
                            fontFamily = FontFamily.SansSerif
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(onClick = {
                            startActivity(Intent(this@SplashScreen, SignupActivity::class.java))
                            overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                        },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                        ) {
                            Text(text = "BEGIN", fontWeight = FontWeight.Black, fontSize = 20.sp, color = Color.Black)//,Modifier.padding(start = 10.dp))
                        }



/*
                TextButton(
                    onClick = {
                        startActivity(Intent(this@SplashScreen, MainActivity::class.java));
                        finish();
                    },
                ){
                    Text("Proceed")
                    Icon(
                        Icons.Filled.ArrowForward,
                        contentDescription = "Continue",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                }
*/
                    }
                }
            }
        )
    }

    @Preview
    @Composable
    fun DefaultPreview(){
        MainScreen()
    }
}