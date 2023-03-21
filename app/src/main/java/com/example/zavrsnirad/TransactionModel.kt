package com.example.zavrsnirad

data class TransactionModel(
    var transactionName: String? = null,
    var transactionType: String? = null,
    var transactionValue: Double? = null,
    var transactionDate: String? = null,
    var balanceAfterTransaction: Double? = null
)