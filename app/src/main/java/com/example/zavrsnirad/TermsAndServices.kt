package com.example.zavrsnirad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zavrsnirad.ui.theme.BGGray
import com.example.zavrsnirad.ui.theme.NormalText
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Suppress("OverrideDeprecatedMigration")
class TermsAndServices : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(
                color = BGGray
            )
            systemUiController.setStatusBarColor(
                color = BGGray
            )

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BGGray,
                ){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ){
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(.15f)
                                .padding(10.dp),
                            shape = RoundedCornerShape(10.dp)
                        ){
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ){

                                Button(
                                    modifier = Modifier,
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                                    elevation = ButtonDefaults.elevation(
                                        defaultElevation = 0.dp,
                                        pressedElevation = 0.dp,
                                        disabledElevation = 0.dp
                                    ),
                                    onClick = {
                                        overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                                        finish()
                                    }
                                ){
                                    Icon(
                                        Icons.Filled.ArrowBackIosNew,
                                        null,
                                        Modifier,
                                        Color.Black
                                    )
                                }

                                Text(
                                    modifier = Modifier
                                        .padding(10.dp),
                                    text = "Terms of Service",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Black,
                                    color = NormalText,
                                )
                            }
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(.9f)
                                .padding(10.dp),
                            shape = RoundedCornerShape(10.dp)
                        ){
                            Text(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .verticalScroll(rememberScrollState()),
                                text = stringResource(R.string.terms_of_service),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = NormalText,
                            )
                        }
                    }
                }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
    }
}