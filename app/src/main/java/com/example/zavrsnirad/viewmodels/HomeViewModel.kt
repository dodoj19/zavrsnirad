package com.example.zavrsnirad.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.zavrsnirad.TransactionModel
import com.example.zavrsnirad.UserModel
import com.example.zavrsnirad.sealed.DataState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeViewModel : ViewModel(){

    val response: MutableState<DataState> = mutableStateOf(DataState.Empty)

    init{
        fetchDataFromDatabase()
    }

    private fun fetchDataFromDatabase() {
        val mAuth = Firebase.auth
        val mDatabase = Firebase.database("https://zavrsnirad-1e613-default-rtdb.europe-west1.firebasedatabase.app/")
        var tempModel = UserModel()
        val user = mAuth.currentUser
        response.value = DataState.Loading

        mDatabase.getReference("Users").child(user!!.uid).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                tempModel.userName = snapshot.child("userName").getValue(String::class.java)
                tempModel.userGender = snapshot.child("userGender").getValue(String::class.java)
                tempModel.userJoin = snapshot.child("userName").getValue(String::class.java)
                tempModel.userId = snapshot.child("userName").getValue(String::class.java)
                tempModel.userBalance = snapshot.child("userBalance").getValue(Double::class.java)

                val list = mutableListOf<LinkedHashMap<String, TransactionModel>>()
                for (ds in snapshot.child("userTransactionHistory").children){

                    val snapshotKey = ds.key
                    val transaction: TransactionModel? = ds.getValue(TransactionModel::class.java)

                    Log.d("DATABASE", snapshotKey!!)
                    Log.d("DATABASE", transaction!!.transactionName!!)
                    Log.d("DATABASE", transaction.transactionType!!)
                    Log.d("DATABASE", transaction.transactionValue!!.toString())

                    val hash = linkedMapOf(Pair(snapshotKey, transaction))

                    list.add(hash)
                }

                tempModel.userTransactionHistory = list

                response.value = DataState.Success(tempModel)
            }

            override fun onCancelled(error: DatabaseError) {
                response.value = DataState.Failure(error.message)
            }

        })
    }

}