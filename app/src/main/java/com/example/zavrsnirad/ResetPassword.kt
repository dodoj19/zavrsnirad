package com.example.zavrsnirad

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ResetPassword : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            DefaultPreview()
        }
    }

    private fun InitiatePasswordReset(email: String){
        Firebase.auth.sendPasswordResetEmail(email.replace(" ", ""))
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "Email sent successfully!", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(baseContext, "Account not found!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    @Override
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
    }

    @Composable
    fun Screen(){

        val systemUiController = rememberSystemUiController()
        systemUiController.setStatusBarColor(
            color = Color.hsv(34f, 0.82f, 0.99f)
        )

        Box(
            modifier = Modifier
                .background(color = Color.White)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ){
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(200.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors =
                                listOf(
                                    Color.hsv(34f, 0.82f, 0.99f),
                                    Color.hsv(12f, 0.90f, 0.66f)
                                )
                            )
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Image(painter = painterResource(id = R.drawable.splash_logo), contentDescription = "Logo")
                }


                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxHeight(.7f), verticalArrangement = Arrangement.SpaceEvenly){
                    Text(
                        text = "Reset password",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Black,
                        fontSize = 35.sp,
                        fontFamily = FontFamily.Default,
                        color = Color.DarkGray,
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    var emailtxt by remember { mutableStateOf(TextFieldValue("")) }

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
                        placeholder = { Text(text = "Enter your e-mail") },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.White,
                            focusedIndicatorColor = Color.hsv(12f, 0.90f, 0.66f),
                            focusedLabelColor = Color.hsv(12f, 0.90f, 0.66f),
                            cursorColor = Color.hsv(12f, 0.90f, 0.66f)
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.hsv(34f, 0.82f, 0.99f)),
                        onClick = {
                                  InitiatePasswordReset(emailtxt.text)
                        },
                    ){

                        Text(
                            text = "RESET",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    Column(horizontalAlignment = Alignment.CenterHorizontally){
                        ClickableText(
                            text = buildAnnotatedString{
                                append("Don't have an account? ")
                                withStyle(style = SpanStyle(color = Color.hsv(34f, 0.82f, 0.99f), fontWeight = FontWeight.Black)) {
                                    append("SIGN UP")
                                }
                            },
                            modifier = Modifier,
                            style = TextStyle(
                                color = Color.DarkGray
                            ),
                            onClick = {
                                startActivity(Intent(this@ResetPassword, SignupActivity::class.java))
                                overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                                finishAfterTransition()
                            }
                        )

                        ClickableText(
                            text = buildAnnotatedString{
                                append("Go back to ")
                                withStyle(style = SpanStyle(color = Color.hsv(34f, 0.82f, 0.99f), fontWeight = FontWeight.Black)) {
                                    append("LOG IN")
                                }
                            },
                            modifier = Modifier,
                            style = TextStyle(
                                color = Color.DarkGray
                            ),
                            onClick = {
                                startActivity(Intent(this@ResetPassword, LoginActivity::class.java))
                                overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                                finishAfterTransition()
                            }
                        )
                    }
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(60.dp)
                        .weight(1f, false)
                        .background( color = Color.hsl(70f, 0f, 0.90f, 1f))
                ){
                    ClickableText(
                        text = buildAnnotatedString{
                            append("By signing up you agree to our ")
                            withStyle(style = SpanStyle(color = Color.Blue)) {
                                append("Terms of Services")
                            }
                            append(" and acknowledge our ")
                            withStyle(style = SpanStyle(color = Color.Blue)) {
                                append("Privacy Policy")
                            }
                            append(" describing how we handle your personal data.")
                        },
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            color = Color.hsl(70f, 0f, 0.50f, 1f)
                        ),
                        onClick = {
                            startActivity(Intent(this@ResetPassword, TermsAndServices::class.java))
                            overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                        }
                    )
                }

            }

        }
    }


    @Preview
    @Composable
    fun DefaultPreview(){
        Screen()
    }
}