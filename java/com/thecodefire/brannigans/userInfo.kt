package com.thecodefire.brannigans

class userInfo {
    var name: String? = null
    var email: String? = null
    var password: String? = null
    var status: String? = null
    constructor(){}

    constructor(name: String, email: String, password: String, status: String){
        this.email = email
        this.name = name
        this.password = password
        this.status = status
    }
}