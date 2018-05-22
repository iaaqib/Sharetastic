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

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.raywenderlich.sharetastic.model.SocialNetwork
import com.raywenderlich.sharetastic.model.UserModel
import com.squareup.picasso.Picasso
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.*
import android.text.Editable
import android.text.TextWatcher
import android.text.InputFilter
import android.view.View
import com.facebook.FacebookSdk
import com.raywenderlich.android.sharetastic.R
import kotlinx.android.synthetic.main.activity_share.nameTextView
import kotlinx.android.synthetic.main.activity_share.userNameTextView
import kotlinx.android.synthetic.main.activity_share.connectedWithTextView
import kotlinx.android.synthetic.main.activity_share.postEditText
import kotlinx.android.synthetic.main.activity_share.profileImageView
import kotlinx.android.synthetic.main.activity_share.postButton
import kotlinx.android.synthetic.main.activity_share.characterLimitTextView


class ShareActivity : AppCompatActivity() {


    lateinit var user: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        postButtonAction()
        user = intent.extras.get("user") as UserModel
        setData(user)
    }


    fun postButtonAction(){

        postButton.setOnClickListener{view ->

          if(postEditText.text.equals("")) {

              Toast.makeText(this,R.string.cannot_be_empty,Toast.LENGTH_SHORT).show()

          }else if (user.socialNetwork == SocialNetwork.Facebook){
            postStatusToFacebook(postEditText.text.toString())

          }else{
           postATweet(postEditText.text.toString())
          }

        }
    }


    fun setData(user: UserModel) {
        nameTextView.text = user.name
        userNameTextView.text = if (user.socialNetwork == SocialNetwork.Twitter)  "@${user.userName}" else user.userName
        connectedWithTextView.text =  if (user.socialNetwork == SocialNetwork.Twitter) "${connectedWithTextView.text}Twitter" else "${connectedWithTextView.text}Facebook"
        characterLimitTextView.visibility =  if (user.socialNetwork == SocialNetwork.Twitter) View.VISIBLE else View.GONE

        Picasso.with(this).load(user.profilePictureUrl).placeholder(R.drawable.ic_user).into(profileImageView)
        //If user is logged in with twitter only then enable the character limit
        if (user.socialNetwork == SocialNetwork.Twitter){
            postEditText.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(240)
            ))
            onTextChangeListener()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_logout,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_logout -> {
                sendToMainActivity()
                return true
            }
            else -> return super.onOptionsItemSelected(item)

        }
    }


    fun postStatusToFacebook(message: String) {

        if(AccessToken.getCurrentAccessToken() != null){
            val params = Bundle()
            params.putString("message", message)

            GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/me/feed",
                    params,
                    HttpMethod.POST,
                    GraphRequest.Callback { response ->
                        if (response.error == null){

                            Toast.makeText(this,R.string.facebook_posted,Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this,response.error.errorMessage,Toast.LENGTH_SHORT).show()
                        }

                    }
            ).executeAsync()
        }
        postEditText.setText("")


    }

    fun postATweet(message: String) {

        val statusesService = TwitterCore.getInstance().apiClient.getStatusesService()
        val context = this
        statusesService.update(message, null, null, null, null, null, null, null, null).enqueue(object : Callback<Tweet>() {
            override fun success(result: Result<Tweet>) {
                Toast.makeText(context,R.string.tweet_posted,Toast.LENGTH_SHORT).show()
            }

            override  fun failure(exception: TwitterException) {

                Toast.makeText(context,exception.localizedMessage,Toast.LENGTH_SHORT).show()

            }
        })
        postEditText.setText("")

    }
    //For twitter when user types the character count needs to be changed
    fun onTextChangeListener() {
        postEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                characterLimitTextView.setText("${s.length}/240")
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

            }
        })

    }

    fun sendToMainActivity() {
        if (user.socialNetwork == SocialNetwork.Facebook) {
            LoginManager.getInstance().logOut()

        }else {
            TwitterCore.getInstance().sessionManager.clearActiveSession()
        }
        finish()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}