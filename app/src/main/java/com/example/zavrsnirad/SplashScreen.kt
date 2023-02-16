package com.example.zavrsnirad

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Thread.sleep
import java.net.URL
import javax.net.ssl.HttpsURLConnection

// SETTINGS
private var LOADINGSECONDS = 3

class SplashScreen : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    fun moveToSignup(){
        startActivity(Intent(this@SplashScreen, SignupActivity::class.java))
        overridePendingTransition(
            androidx.appcompat.R.anim.abc_fade_in,
            androidx.appcompat.R.anim.abc_fade_out
        )
    }

    fun moveToHome(){
        startActivity(Intent(this@SplashScreen, HomeScreen::class.java))
        overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
        finishAfterTransition()
    }

    fun moveToNoConnection(){
        setContent{
            NoInternetScreen()
        }
    }

    private fun isDeviceOnline(context: Context): Boolean {
        val connManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connManager.getNetworkCapabilities(connManager.activeNetwork)
        return if (networkCapabilities == null) {
            false
        } else {
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_SUSPENDED)
        }
    }

    private fun isInternetAvailable(): Boolean {
        val urlConnection =
            URL("https://clients3.google.com/generate_204").openConnection() as HttpsURLConnection
        try {
            urlConnection.setRequestProperty("User-Agent", "Android")
            urlConnection.setRequestProperty("Connection", "close")
            urlConnection.connectTimeout = 1000
            urlConnection.connect()
            return urlConnection.responseCode == 204
        } catch (e: Exception) {
            return false
        }
    }

    private fun runActivityDetermineCheck(){
        val deviceAbleToCommunicate = isDeviceOnline(this.applicationContext) || isInternetAvailable()
        if(deviceAbleToCommunicate){
            val currentUser = auth.currentUser
            if (currentUser != null){
                moveToHome()
            }else{
                moveToSignup()
            }
        }
        else{
            moveToNoConnection()
        }
    }

    override fun onStart() {
        super.onStart()
        runActivityDetermineCheck();
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun NoInternetScreen() {

        val systemUiController = rememberSystemUiController()
        systemUiController.setSystemBarsColor(
            color = Color.DarkGray
        )
        systemUiController.setStatusBarColor(
            color = Color.DarkGray
        )

        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = Color.DarkGray,
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row(
                    modifier = Modifier.
                    requiredHeight(200.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Icon(Icons.Filled.WifiOff, null, Modifier.size(150.dp), Color.White)
                }

                Text(
                    text = "No connection",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(10.dp),
                    fontSize = 45.sp,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "You need internet connection to use this application.",
                    color = Color.LightGray,
                    modifier = Modifier.padding(10.dp),
                    fontSize = 19.sp,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center
                )
                IconButton(onClick = {



                    runActivityDetermineCheck()
                }) {
                    Icon(Icons.Filled.Refresh, null, Modifier.size(40.dp), Color.LightGray)
                }
            }
        }
    }
}