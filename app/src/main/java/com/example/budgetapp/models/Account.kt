package com.example.budgetapp.models

class Account {
    private var accountAmount: Double = 0.0
    var accountName: String? = null

    constructor()

    constructor(accountAmount: Int, accountName: String?) {
        this.accountAmount = accountAmount.toDouble()
        this.accountName = accountName
    }
}