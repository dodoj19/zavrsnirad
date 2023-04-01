package com.example.zavrsnirad

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zavrsnirad.sealed.DataState
import com.example.zavrsnirad.ui.theme.BGGray
import com.example.zavrsnirad.viewmodels.HomeViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AlterProfileActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()

    private var alterType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        alterType = intent.extras!!.getString("Selection")


        onBackPressedDispatcher.addCallback(this ) {
            finishAffinity()
            startActivity(Intent(this@AlterProfileActivity, ProfileActivity::class.java))
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
                    verticalArrangement = Arrangement.SpaceEvenly,
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
                                finishAffinity()
                                startActivity(Intent(this@AlterProfileActivity, ProfileActivity::class.java))
                            }
                        ){
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null,
                                tint = Color.DarkGray,
                                modifier = Modifier.size(48.dp)
                            )
                        }

                        Spacer(Modifier.width(15.dp))

                        Text(
                            text = "RETURN",
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
                AlterInfo(result.data)
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
    fun AlterInfo(data: UserModel) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(5.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            when(alterType!!){
                "Name" -> {
                    ChangeName()
                }

                "Gender" ->{
                    ChangeGender(data.userGender!!)
                }

                "Email" ->{
                    ChangeEmail()
                }

                "Password" ->{
                    ChangePassword()
                }

            }

        }
    }

    private @Composable
    fun ChangePassword() {
        var password by rememberSaveable { mutableStateOf("") }
        var passwordVisible by rememberSaveable { mutableStateOf(false) }

        var confirmPassword by rememberSaveable{ mutableStateOf("") }
        var confirmPasswordVisible by rememberSaveable{ mutableStateOf(false) }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(text = "Change your password:", color = Color.DarkGray, fontSize = 30.sp, fontWeight = FontWeight.Bold, fontFamily = latoFontFamily, textAlign = TextAlign.Center)
        }

        Spacer(Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
            }
        }

        Spacer(Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            OutlinedButton(
                modifier = Modifier.size(height= 50.dp, width = 180.dp),
                border = BorderStroke(2.dp, Color(0xFF40A341)),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = Color.DarkGray),
                onClick = {
                    if (password == confirmPassword){
                        AttemptPaswordChange(password)
                    }
                    else{
                        Toast.makeText(this@AlterProfileActivity, "Passwords aren't matching!", Toast.LENGTH_SHORT).show()
                    }

                },
            ) {
                Text(
                    text = "CONFIRM",
                    color = Color(0xFF40A341),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = latoFontFamily
                )
            }
        }
    }

    private fun AttemptPaswordChange(pass: String) {
        val sharedPref = application.getSharedPreferences("UserDetails", Context.MODE_PRIVATE) ?: return

        val password = pass.replace(" ", "")

        if (password != ""){
            val mAuth = Firebase.auth
            val pass = sharedPref.getString(getString(R.string.password_key), "")

            mAuth.signInWithEmailAndPassword(mAuth.currentUser!!.email!!, pass!!).addOnCompleteListener(this){ task ->
                if (task.isSuccessful){
                    mAuth.currentUser!!.updatePassword(password).addOnCompleteListener {act ->
                        if(act.isSuccessful){
                            Toast.makeText(this@AlterProfileActivity, "Password changed successfully!", Toast.LENGTH_SHORT).show()

                            // Save password
                            with (sharedPref.edit()) {
                                putString(getString(R.string.password_key), password)
                                apply()
                            }

                            setContent {
                                finishAffinity()
                                startActivity(Intent(this@AlterProfileActivity, ProfileActivity::class.java))
                            }
                        }
                    }
                }
            }
        }
    }

    private @Composable
    fun ChangeEmail() {
        var newEmail by rememberSaveable { mutableStateOf("") }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(text = "Change your email:", color = Color.DarkGray, fontSize = 32.sp, fontWeight = FontWeight.Bold, fontFamily = latoFontFamily)
        }

        Spacer(Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            OutlinedTextField(
                value = newEmail,
                onValueChange = {
                    newEmail = it
                },
                leadingIcon = {Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "emailIcon")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.padding(5.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.DarkGray,
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = Color.DarkGray
                ),
                shape = RoundedCornerShape(20.dp)
            )
        }

        Spacer(Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            OutlinedButton(
                modifier = Modifier.size(height= 50.dp, width = 180.dp),
                border = BorderStroke(2.dp, Color(0xFF40A341)),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = Color.DarkGray),
                onClick = {
                    AttemptEmailChange(newEmail)
                },
            ) {
                Text(
                    text = "CONFIRM",
                    color = Color(0xFF40A341),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = latoFontFamily
                )
            }
        }

    }

    private fun AttemptEmailChange(newEmail: String) {
        val sharedPref = application.getSharedPreferences("UserDetails", Context.MODE_PRIVATE) ?: return

        val email = newEmail.replace(" ", "")

        if (email != ""){
            if (email.length >= 10){
                if(Regex("@").containsMatchIn(email)) {
                    val mAuth = Firebase.auth
                    val pass = sharedPref.getString(getString(R.string.password_key), "")

                    Log.d("****DEBUG****", pass!!);

                    mAuth.signInWithEmailAndPassword(mAuth.currentUser!!.email!!, pass!!).addOnCompleteListener(this) {task ->
                        Log.d("****DEBUG****", "Test A");

                        if (task.isSuccessful){
                            // Sign-in was successful, attempting change
                            mAuth.currentUser!!.updateEmail(email.replace(" ", "")).addOnCompleteListener(this){act ->
                                if (act.isSuccessful){
                                    Toast.makeText(this@AlterProfileActivity, "Email changed successfully!", Toast.LENGTH_SHORT).show()
                                    setContent {
                                        finishAffinity()
                                        startActivity(Intent(this@AlterProfileActivity, ProfileActivity::class.java))
                                    }
                                }
                                else {
                                    Toast.makeText(this@AlterProfileActivity, "An unknown error occurred!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        else{
                            Toast.makeText(this@AlterProfileActivity, "User re-authentication failed, try logging out maybe?", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else{
                    Toast.makeText(this@AlterProfileActivity, "Your email is not valid!", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this@AlterProfileActivity, "Your email is not long enough!", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this@AlterProfileActivity, "Your email field must not be blank!", Toast.LENGTH_SHORT).show()
        }
    }

    @Composable
    fun ChangeName() {
        var newUsername by rememberSaveable { mutableStateOf("") }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(text = "Change your name:", color = Color.DarkGray, fontSize = 32.sp, fontWeight = FontWeight.Bold, fontFamily = latoFontFamily, textAlign = TextAlign.Center)
        }

        Spacer(Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            OutlinedTextField(
                value = newUsername,
                onValueChange = {
                    newUsername = it
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.padding(5.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.DarkGray,
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = Color.DarkGray
                ),
                shape = RoundedCornerShape(20.dp)
            )
        }

        Spacer(Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            OutlinedButton(
                modifier = Modifier.size(height= 50.dp, width = 180.dp),
                border = BorderStroke(2.dp, Color(0xFF40A341)),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = Color.DarkGray),
                onClick = {
                    if (newUsername != ""){
                        if (newUsername.length >= 4){
                            if(!Regex("[^A-Za-z0-9 ]").containsMatchIn(newUsername)){
                                if(!Regex(" ").containsMatchIn(newUsername)){
                                    if(newUsername.length < 15){
                                        val mAuth = Firebase.auth
                                        val mDatabase = Firebase.database("https://zavrsnirad-1e613-default-rtdb.europe-west1.firebasedatabase.app/")
                                        val mUser = mAuth.currentUser

                                        mDatabase.getReference("Users").child(mUser!!.uid).child("userName").setValue(newUsername).addOnCompleteListener {
                                            setContent {
                                                finishAffinity()
                                                startActivity(Intent(this@AlterProfileActivity, ProfileActivity::class.java))
                                            }
                                        }
                                    }
                                    else{
                                        Toast.makeText(this@AlterProfileActivity, "You must have less than 15 characters!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                else{
                                    Toast.makeText(this@AlterProfileActivity, "Your username must not contain any spaces.", Toast.LENGTH_SHORT).show()
                                }

                            }
                            else{
                                Toast.makeText(this@AlterProfileActivity, "Your username must not contain any special characters.", Toast.LENGTH_SHORT).show()
                            }

                        }
                        else{
                            Toast.makeText(this@AlterProfileActivity, "Your username should contain at least 4 characters.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(this@AlterProfileActivity, "Your Username field must not be blank!", Toast.LENGTH_SHORT).show()
                    }
                },
            ) {
                Text(
                    text = "CONFIRM",
                    color = Color(0xFF40A341),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = latoFontFamily
                )
            }
        }
    }

    @Composable
    fun ChangeGender(currentGender: String) {

        val radioOptions = listOf("Male", "Female", "Other")
        var alreadyIndex = 0
        if (radioOptions.indexOf(currentGender) != -1){
            alreadyIndex = radioOptions.indexOf(currentGender)
        }
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[alreadyIndex] ) }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(text = "Choose your gender:", color = Color.DarkGray, fontSize = 32.sp, fontWeight = FontWeight.Bold, fontFamily = latoFontFamily, textAlign = TextAlign.Center)
        }

        Spacer(Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Column {
                radioOptions.forEach { text ->
                    Row(
                        Modifier
                            .fillMaxWidth(.5f)
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = {
                                    onOptionSelected(text)
                                }
                            ),

                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            colors = RadioButtonDefaults.colors(
                              selectedColor = Color(0xFF6CC96D)
                            ),
                            selected = (text == selectedOption),
                            onClick = { onOptionSelected(text) }
                        )
                        Text(
                            text = text,
                            color = Color.DarkGray,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = latoFontFamily
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            OutlinedButton(
                modifier = Modifier.size(height= 50.dp, width = 180.dp),
                border = BorderStroke(2.dp, Color(0xFF40A341)),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = Color.DarkGray),
                onClick = {
                    updateGender(selectedOption)
                }
            ) {
                Text(
                    text = "CONFIRM",
                    color = Color(0xFF40A341),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = latoFontFamily
                )
            }
        }
    }

    private fun updateGender(selectedGender: String) {
        val mAuth = Firebase.auth
        val mDatabase = Firebase.database("https://zavrsnirad-1e613-default-rtdb.europe-west1.firebasedatabase.app/")
        val mUser = mAuth.currentUser

        mDatabase.getReference("Users").child(mUser!!.uid).child("userGender").setValue(selectedGender).addOnCompleteListener {
            setContent {
                finishAffinity()
                startActivity(Intent(this@AlterProfileActivity, ProfileActivity::class.java))
                Toast.makeText(this@AlterProfileActivity, "Gender changed!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
