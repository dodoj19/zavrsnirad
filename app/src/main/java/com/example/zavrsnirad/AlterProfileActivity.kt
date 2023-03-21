package com.example.zavrsnirad

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zavrsnirad.sealed.DataState
import com.example.zavrsnirad.ui.theme.BGGray
import com.example.zavrsnirad.ui.theme.NormalText
import com.example.zavrsnirad.viewmodels.HomeViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class AlterProfileActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

            var newUsername by rememberSaveable { mutableStateOf("") }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(text = "Change your name:", color = Color.DarkGray, fontSize = 32.sp, fontWeight = FontWeight.Bold, fontFamily = latoFontFamily)
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
                                                    startActivity(Intent(this@AlterProfileActivity, HomeScreen::class.java))
                                                    finish()
                                                    Toast.makeText(this@AlterProfileActivity, "Username changed!", Toast.LENGTH_SHORT).show()
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
    }
}
