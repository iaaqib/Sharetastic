package com.kodesnippets.sharetastic

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.facebook.AccessToken
import com.kodesnippets.sharetastic.Utils.Helper
import com.twitter.sdk.android.core.TwitterCore
import kotlin.concurrent.thread

/**
 * Created by aaqibhussain on 10/2/18.
 */
class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        thread {

            Thread.sleep((3 * 1000).toLong())
            startActivityOnBasisOfSession()


        }.priority = Thread.NORM_PRIORITY
    }

    fun startActivityOnBasisOfSession(){
        if(AccessToken.getCurrentAccessToken() != null){
            Helper.getFacebookUserProfieWithGraphApi(this)
        }else if (TwitterCore.getInstance().sessionManager.activeSession != null){

            Helper.getTwitterUserProfileWthTwitterCoreApi(this, TwitterCore.getInstance().sessionManager.activeSession)
        }
        else {
           startActivity()
        }

    }
    fun startActivity(){
        var intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}