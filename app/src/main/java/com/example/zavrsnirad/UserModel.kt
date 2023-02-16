package com.example.zavrsnirad

data class UserModel (
    var userId: String? = null,
    var userName: String? = null,
    var userBalance: Double? = null,
    var userJoin: String? = null,
    var userGender: String? = null,
    var userTransactionHistory: MutableList<HashMap<String, TransactionModel>>? = null
)