package com.example.hotel

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hotel.auth.AuthMananger
import com.example.hotel.common.HotelActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {
    private val authMananger: AuthMananger by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnSignIn.setOnClickListener{ signIn() }
        checkGooglePlayServices()
    }

    private fun signIn() {
        val signInIntent = authMananger.getSignInIntent()
        startActivityIfNeeded(signInIntent, REQUEST_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == REQUEST_SIGN_IN){
            if(resultCode == Activity.RESULT_OK){
                handleSignResult(data)
            }
        } else if(resultCode == REQUEST_PLAY_SERVICES){
            checkGooglePlayServices()
        }
    }

    private fun handleSignResult(intent: Intent?){
        try {
            GoogleSignIn.getSignedInAccountFromIntent(intent).getResult(ApiException::class.java)
            startActivity(Intent(this, HotelActivity::class.java))
            finish()
        }catch (e: ApiException){
            Toast.makeText(this, R.string.error_login_failed, Toast.LENGTH_LONG).show()
        }
    }

    private fun checkGooglePlayServices(){
        val api = GoogleApiAvailability.getInstance()

        val resultCode = api.isGooglePlayServicesAvailable(this)
        if(resultCode != ConnectionResult.SUCCESS){
            if(api.isUserResolvableError(resultCode)){
                val dialog = api.getErrorDialog(this, resultCode, REQUEST_PLAY_SERVICES)
                dialog?.setOnCancelListener{
                    finish()
                }
                dialog?.show()
            } else {
                Toast.makeText(this, R.string.error_play_services_not_supported, Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    companion object{
        private const val REQUEST_SIGN_IN = 1000
        private const val REQUEST_PLAY_SERVICES = 2000
    }
}