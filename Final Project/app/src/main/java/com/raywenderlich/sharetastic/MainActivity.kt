/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.raywenderlich.sharetastic

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.facebook.*
import com.facebook.login.LoginResult
import java.util.*
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.TwitterException
import android.widget.Toast
import com.facebook.login.LoginManager
import com.raywenderlich.android.sharetastic.R
import com.raywenderlich.sharetastic.utils.Helper
import kotlinx.android.synthetic.main.activity_main.twitterLoginButton
import kotlinx.android.synthetic.main.activity_main.facebookLoginButton


class MainActivity : AppCompatActivity() {

    val EMAIL = "email"
    val PUBLIC_PROFILE = "public_profile"
    val USER_PERMISSION = "user_friends"

    lateinit var callbackManager: CallbackManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        facebookSetup(this)
        twitterSetup(this)


    }

    fun facebookSetup(context: Context) {

        callbackManager = CallbackManager.Factory.create()
        facebookLoginButton.setReadPermissions(Arrays.asList(EMAIL, PUBLIC_PROFILE, USER_PERMISSION))
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile", "user_friends"))
        facebookLoginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Helper.getFacebookUserProfileWithGraphApi(context)
            }

            override fun onCancel() {

            }

            override fun onError(exception: FacebookException) {
               
                Toast.makeText(context,exception.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })



    }

    fun twitterSetup(context: Context) {

        twitterLoginButton.setCallback(object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>) {

            Helper.getTwitterUserProfileWthTwitterCoreApi(context, result.data)

            }

            override fun failure(exception: TwitterException) {
                
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
