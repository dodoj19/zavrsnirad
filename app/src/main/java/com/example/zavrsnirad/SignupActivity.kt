package com.example.zavrsnirad

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class SignupActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseUsers: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this ) {
            overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
        }

        auth = Firebase.auth
        database = Firebase.database("https://zavrsnirad-1e613-default-rtdb.europe-west1.firebasedatabase.app/")
        setContent{
            DefaultPreview()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)){view, insets ->
            val bottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            view.updatePadding(bottom = bottom)
            insets
        }
    }

    private fun initiateAccountCreation(email: String, password: String, passwordConfirm: String){
        // Formation check
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

                user = auth.currentUser!!

                val dbRef = database.getReference("Users")
                val userNew = UserModel(user.uid, user.email.toString().split("@")[0], 0.00, Date().time.toString(), "Null")

                dbRef.child(user.uid).setValue(userNew)
                    .addOnCompleteListener{
                        startActivity(Intent(this@SignupActivity, HomeScreen::class.java))
                        overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                        finishAfterTransition()
                    }
                    .addOnFailureListener{err ->
                        Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
                    }

                val transactionNew = TransactionModel("Kupnja u Mc Donaldsu", "Hrana", -3.0)

                dbRef.child(user.uid).child("userTransactionHistory").child("Transaction").setValue(transactionNew)
                    .addOnCompleteListener {
                        Toast.makeText(this, "Uspjesno dodano!", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {err ->
                        Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            else{
                Log.w("TESTINGTAG", "createUserWithEmail:failure", task.exception)
                Toast.makeText(baseContext, "An error while creating your account!", Toast.LENGTH_SHORT).show()
            }
        }
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
                        onClick = { initiateAccountCreation(emailtxt.text, password, confirmPassword) },
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
                        .background(color = Color.hsl(70f, 0f, 0.90f, 1f))
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
                            startActivity(Intent(this@SignupActivity, TermsAndServices::class.java))
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