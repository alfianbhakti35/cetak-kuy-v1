package com.efix.cetakkuy.auth.model

class Users(val uid : String, val nama : String, val email : String, val noHp : String, val alamat : String, val kodePos : String, val password : String, val role : String) {
    constructor(): this("", "", "", "", "", "", "", "")
}