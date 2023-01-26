package com.example.zavrsnirad

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignupActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContent{
            DefaultPreview()
        }
    }

    private fun InitiateAccountCreation(email: String, password: String, passwordConfirm: String){
        if (email == ""){
            Toast.makeText(baseContext, "Your email must not be empty!", Toast.LENGTH_SHORT).show()
            return
        }

        if (password == ""){
            Toast.makeText(baseContext, "Your password must not be empty!", Toast.LENGTH_SHORT).show()
            return
        }

        if (passwordConfirm == ""){
            Toast.makeText(baseContext, "You must confirm your password", Toast.LENGTH_SHORT).show()
            return
        }

        if (passwordConfirm != password){
            Toast.makeText(baseContext, "Passwords don't match!", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email.replace(" ", ""), password.replace(" ", "")).addOnCompleteListener(this){ task ->
            if(task.isSuccessful){
                // Sign in success, update UI with the signed-in user's information
                Log.d("TESTINGTAG", "createUserWithEmail:success")
                startActivity(Intent(this@SignupActivity, HomeScreen::class.java))
                overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                finishAfterTransition()
            }
            else{
                // If sign in fails, display a message to the user.
                Log.w("TESTINGTAG", "createUserWithEmail:failure", task.exception)
                Toast.makeText(baseContext, "An error while creating your account!", Toast.LENGTH_SHORT).show()
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
            color = MaterialTheme.colors.primary
        )

        Box(
            modifier = Modifier
                .background(color = Color.White)
                .fillMaxSize()
        ){
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth(1.0f)
                        .requiredHeight(200.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors =
                                listOf(
                                    MaterialTheme.colors.primary,
                                    MaterialTheme.colors.primaryVariant
                                )
                            )
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Image(painter = painterResource(id = R.drawable.splash_logo), contentDescription = "Logo")
                }
                Text(
                    text = "Sign up below to start saving today!",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Black,
                    fontSize = 35.sp,
                    fontFamily = FontFamily.Default,
                    color = Color.DarkGray,
                )


                var emailtxt by remember { mutableStateOf(TextFieldValue("")) }
                var password by rememberSaveable { mutableStateOf("") }
                var passwordVisible by rememberSaveable { mutableStateOf(false) }

                var confirmPassword by rememberSaveable{ mutableStateOf("") }
                var confirmPasswordVisible by rememberSaveable{ mutableStateOf(false) }

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
                        modifier = Modifier.padding(5.dp)
                    )

                    OutlinedTextField(
                        value = password,
                        singleLine = true,
                        onValueChange = {
                            password = it
                        },
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
                            val description = if (passwordVisible) "Hide password" else "Show password"
                            IconButton(onClick = {passwordVisible = !passwordVisible}){
                                Icon(imageVector  = image, description)
                            }
                        },
                        modifier = Modifier.padding(5.dp)
                    )

                    OutlinedTextField(
                        value = confirmPassword,
                        singleLine = true,
                        onValueChange = {
                            confirmPassword = it
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        label = { Text(text = "Confirm password") },
                        placeholder = { Text(text = "Enter your password") },
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (confirmPasswordVisible) {
                                Icons.Filled.Visibility
                            }
                            else {
                                Icons.Filled.VisibilityOff
                            }
                            val description = if (confirmPasswordVisible) "Hide password" else "Show password"
                            IconButton(onClick = {confirmPasswordVisible = !confirmPasswordVisible}){
                                Icon(imageVector  = image, description)
                            }
                        },
                        modifier = Modifier.padding(5.dp)
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                        onClick = { InitiateAccountCreation(emailtxt.text, password, confirmPassword) },
                    ){

                        Text(
                            text = "CREATE",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                ClickableText(
                    text = buildAnnotatedString{
                        append("Already have an account? ")
                        withStyle(style = SpanStyle(color = Color.Blue, fontWeight = FontWeight.Black)) {
                            append("LOG IN")
                        }
                    },
                    modifier = Modifier,
                    style = TextStyle(
                        color = Color.DarkGray
                    ),
                    onClick = {
                        startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                        overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                        finishAfterTransition()
                    }
                )

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
                        onClick = {}
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