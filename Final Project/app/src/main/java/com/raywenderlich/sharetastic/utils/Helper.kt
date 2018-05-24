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


package com.raywenderlich.sharetastic.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.raywenderlich.sharetastic.ShareActivity
import com.raywenderlich.sharetastic.model.SocialNetwork
import com.raywenderlich.sharetastic.model.UserModel
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.models.User
import java.util.*


object Helper {



    fun getFacebookUserProfileWithGraphApi(context: Context){

        if (AccessToken.getCurrentAccessToken() != null){
            val request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken()
            ) { jsonObject, response ->
                val email = jsonObject?.get("email")?.toString() ?: ""
                val name = jsonObject?.get("name").toString() ?: ""
                val profileObjectImage = jsonObject?.getJSONObject("picture")?.getJSONObject("data")?.get("url").toString() ?: ""
                val user = UserModel(name, email, profileObjectImage, SocialNetwork.Facebook)
                startActivity(context, user)
            }
            val parameters = Bundle()
            parameters.putString("fields", "id,name,link,picture.type(large), email")
            request.parameters = parameters
            request.executeAsync()
        }
    }
    
    fun getTwitterUserProfileWthTwitterCoreApi(context: Context, session: TwitterSession) {

        TwitterCore.getInstance().getApiClient(session).accountService.verifyCredentials(true, true, false).enqueue(object : Callback<User>() {
            override fun success(result: Result<User>) {
                val name = result.data.name
                val userName = result.data.screenName
                val profileImageUrl = result.data.profileImageUrl.replace("_normal", "")
                val user = UserModel(name, userName, profileImageUrl, SocialNetwork.Twitter)
                startActivity(context, user)


            }

            override fun failure(exception: TwitterException) {
                Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
    
    fun startActivity(context: Context, user: UserModel){
        val activity = context as Activity
        val intent = Intent(context, ShareActivity::class.java)
        intent.putExtra("user",user)
        activity.startActivity(intent)
        activity.finish()
    }
}
