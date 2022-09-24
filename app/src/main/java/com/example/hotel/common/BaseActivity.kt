package com.example.hotel.common

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.hotel.LoginActivity
import com.example.hotel.auth.AuthMananger
import org.koin.android.ext.android.inject

abstract class BaseActivity: AppCompatActivity() {

    val authMananger: AuthMananger by inject()

    override fun onStart() {
        super.onStart()
        verifyUserLoggedIn()
    }

    private fun verifyUserLoggedIn() {
        val account = authMananger.getUserAccount()

        if(account == null){
            startActivity(Intent(this, LoginActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })
            finish()
        }
    }
}