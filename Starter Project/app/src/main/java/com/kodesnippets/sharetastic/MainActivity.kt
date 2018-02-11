package com.kodesnippets.sharetastic

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.facebook.*
import com.facebook.login.widget.LoginButton
import com.twitter.sdk.android.core.identity.TwitterLoginButton



class MainActivity : AppCompatActivity() {

    val EMAIL = "email"
    val PUBLIC_PROFILE = "public_profile"

    lateinit var callbackManager: CallbackManager
    lateinit var facebookLoginButton: LoginButton
    lateinit var twitterLoginButton: TwitterLoginButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpViews()
    }

    fun setUpViews(){
        facebookLoginButton = findViewById(R.id.button_facebook_login)
        twitterLoginButton = findViewById(R.id.button_twitter_login)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }
}
