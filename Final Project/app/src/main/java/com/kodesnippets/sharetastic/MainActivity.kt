package com.kodesnippets.sharetastic

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import java.util.*
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterLoginButton
import com.twitter.sdk.android.core.TwitterException
import android.widget.Toast
import com.kodesnippets.sharetastic.Utils.Helper

class MainActivity : AppCompatActivity() {

    val EMAIL = "email"
    val PUBLIC_PROFILE = "public_profile"
    val USER_PERMISSION = "user_friends"

    lateinit var callbackManager: CallbackManager
    lateinit var facebookLoginButton: LoginButton
    lateinit var twitterLoginButton: TwitterLoginButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        facebookSetup(this)
        twitterSetup(this)


    }

    fun facebookSetup(context: Context){

        callbackManager = CallbackManager.Factory.create()

        facebookLoginButton = findViewById(R.id.button_facebook_login)
        facebookLoginButton.setReadPermissions(Arrays.asList(EMAIL, PUBLIC_PROFILE, USER_PERMISSION))
        facebookLoginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Helper.getFacebookUserProfieWithGraphApi(context)
            }

            override fun onCancel() {

            }

            override fun onError(exception: FacebookException) {
                Log.v("Error", exception.localizedMessage)
                Toast.makeText(context,exception.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })



    }
    fun twitterSetup(context: Context){

        twitterLoginButton = findViewById(R.id.button_twitter_login)
        twitterLoginButton.setCallback(object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>) {

            Helper.getTwitterUserProfileWthTwitterCoreApi(context, result.data)

            }

            override fun failure(exception: TwitterException) {
                Log.v("Error", exception.localizedMessage)
                Toast.makeText(context,exception.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
        twitterLoginButton.onActivityResult(requestCode, resultCode, data)
    }


}
