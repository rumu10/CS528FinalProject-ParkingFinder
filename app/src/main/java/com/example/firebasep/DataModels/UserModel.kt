package com.example.firebasep.DataModels

data class UserModel (
    var uId: String,
    var uName: String,
    var uEmail: String,
    var uPass: String,
    var type: String = "User"
)