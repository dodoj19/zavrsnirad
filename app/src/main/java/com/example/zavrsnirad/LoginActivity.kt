package com.example.zavrsnirad

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : ComponentActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            DefaultPreview()
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth = Firebase.auth
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    private fun InitiateLogin(email: String, password: String){

        val sharedPref = application.getSharedPreferences("UserDetails", Context.MODE_PRIVATE) ?: return

        if (email == ""){
            Toast.makeText(baseContext, "Your email must not be empty!", Toast.LENGTH_SHORT).show()
            return
        }

        if (password == ""){
            Toast.makeText(baseContext, "Your password must not be empty!", Toast.LENGTH_SHORT).show()
            return
        }

        mAuth.signInWithEmailAndPassword(email.replace(" ", ""), password.replace(" ", ""))
            .addOnCompleteListener(this) { task ->

                with (sharedPref.edit()) {
                    putString(getString(R.string.password_key), password.replace(" ", ""))
                    apply()
                }

                if (task.isSuccessful) {
                    Log.d("LOGINTESTTAG", "signInWithEmail:success")
                    startActivity(Intent(this@LoginActivity, HomeScreen::class.java))
                    overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                    finishAfterTransition()
                } else {
                    Log.w("LOGINTESTTAG", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
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
            color = Color.hsv(199f, 0.82f, 0.70f)
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
                        .fillMaxWidth(1.0f)
                        .requiredHeight(200.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors =
                                listOf(
                                    Color.hsv(199f, 0.82f, 0.70f),
                                    Color.hsv(220f, 0.90f, 0.55f)
                                )
                            )
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Image(painter = painterResource(id = R.drawable.splash_logo), contentDescription = "Logo")
                }
                Text(
                    text = "Welcome back!",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Black,
                    fontSize = 30.sp,
                    fontFamily = FontFamily.Default,
                    color = Color.DarkGray,
                )


                var emailtxt by remember { mutableStateOf(TextFieldValue("")) }
                var password by rememberSaveable { mutableStateOf("") }
                var passwordVisible by rememberSaveable { mutableStateOf(false) }

                Column(horizontalAlignment = Alignment.CenterHorizontally){
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
                            focusedIndicatorColor = Color.hsv(199f, 0.82f, 0.70f),
                            focusedLabelColor = Color.hsv(199f, 0.82f, 0.70f),
                            cursorColor = Color.hsv(199f, 0.82f, 0.70f)
                        )
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    OutlinedTextField(
                        value = password,
                        singleLine = true,
                        onValueChange = {
                            password = it
                        },
                        modifier = Modifier.padding(vertical = 10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        label = { Text(text = "Password") },
                        placeholder = { Text(text = "Enter your password") },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisible) {
                                Icons.Filled.Visibility
                            }
                            else {
                                Icons.Filled.VisibilityOff
                            }

                            // Please provide localized description for accessibility services
                            val description = if (passwordVisible) "Hide password" else "Show password"

                            IconButton(onClick = {passwordVisible = !passwordVisible}){
                                Icon(imageVector  = image, description)
                            }
                        }
                        ,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.White,
                            focusedIndicatorColor = Color.hsv(199f, 0.82f, 0.70f),
                            focusedLabelColor = Color.hsv(199f, 0.82f, 0.70f),
                            cursorColor = Color.hsv(199f, 0.82f, 0.70f)
                        )
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.hsv(220f, 0.90f, 0.55f)),
                        onClick = {
                            InitiateLogin(emailtxt.text, password)
                        },
                    ){

                        Text(
                            text = "LOGIN",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                ClickableText(
                    text = AnnotatedString(
                        text = "Forgot password?"
                    ),
                    modifier = Modifier,
                    style = TextStyle(
                        fontWeight = FontWeight.Black,
                        color = Color.Blue,
                        fontSize = 20.sp
                    ),
                    onClick = {
                        startActivity(Intent(this@LoginActivity, ResetPassword::class.java))
                        overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                        finishAfterTransition()
                    }
                )

                ClickableText(
                    text = buildAnnotatedString{
                        append("Don't have an account? ")
                        withStyle(style = SpanStyle(color = Color.Blue, fontWeight = FontWeight.Black)) {
                            append("SIGN UP")
                        }
                    },
                    modifier = Modifier,
                    style = TextStyle(
                        color = Color.DarkGray
                    ),
                    onClick = {
                        startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
                        overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                        finishAfterTransition()
                    }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(60.dp)
                        .weight(1f, false)
                        .background(color = Color.hsl(70f, 0f, 0.90f, 1f))
                ){
                    ClickableText(
                        text = buildAnnotatedString{
                            append("By using our services you agree to our ")
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
                            startActivity(Intent(this@LoginActivity, TermsAndServices::class.java))
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