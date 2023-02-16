package com.example.zavrsnirad.sealed

import com.example.zavrsnirad.UserModel

sealed class DataState{
    class Success(val data: UserModel) : DataState()
    class Failure(val message: String) : DataState()
    object Loading : DataState()
    object Empty : DataState()
}