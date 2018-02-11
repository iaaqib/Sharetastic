package com.kodesnippets.sharetastic

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.kodesnippets.sharetastic.model.SocialNetwork
import com.kodesnippets.sharetastic.model.UserModel
import com.squareup.picasso.Picasso
import java.util.*
import com.twitter.sdk.android.core.models.Tweet
import android.provider.SyncStateContract.Helpers.update
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.services.StatusesService
import android.text.Editable
import android.text.TextWatcher
import android.text.InputFilter
import android.view.View


/**
 * Created by aaqibhussain on 10/2/18.
 */
class ShareActivity : AppCompatActivity() {

    lateinit var nameTextView: TextView
    lateinit var userNameTextView: TextView
    lateinit var connectedWithTextView: TextView
    lateinit var profileImageView: ImageView
    lateinit var postEditText: EditText
    lateinit var postButton: Button
    lateinit var characterLimitTextView: TextView

    lateinit var user: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        setUpViews()
        user = intent.extras.get("user") as UserModel
        setData(user)
    }

    fun setUpViews(){
        nameTextView = findViewById(R.id.textView_name)
        userNameTextView = findViewById(R.id.textView_userName)
        connectedWithTextView = findViewById(R.id.textView_connected_with)
        profileImageView = findViewById(R.id.imageView_profile_picture)
        postEditText = findViewById(R.id.editText_post)
        postButton = findViewById(R.id.button_post)
        characterLimitTextView = findViewById(R.id.textView_character_limit)
        action()

    }

    fun action(){
        postButton.setOnClickListener{view ->

          if(postEditText.text.equals("")) {

              Toast.makeText(this,"Message cannot be empty",Toast.LENGTH_SHORT).show()

          }else if (user.socialNetwork == SocialNetwork.Facebook){
            postStatusToFacebook(postEditText.text.toString())

          }else{
           postATweet(postEditText.text.toString())
          }


        }
    }


    fun setData(user: UserModel){
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


    fun postStatusToFacebook(message: String){

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

                            Toast.makeText(this,"Status Posted...",Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this,response.error.errorMessage,Toast.LENGTH_SHORT).show()
                        }

                    }
            ).executeAsync()
        }
        postEditText.setText("")


    }

    fun postATweet(message: String){

        val statusesService = TwitterCore.getInstance().apiClient.getStatusesService()
        val context = this
        statusesService.update(message, null, null, null, null, null, null, null, null).enqueue(object : Callback<Tweet>() {
            override fun success(result: Result<Tweet>) {
                Toast.makeText(context,"Tweeted Sent!!!",Toast.LENGTH_SHORT).show()
            }

            override  fun failure(exception: TwitterException) {

                Toast.makeText(context,exception.localizedMessage,Toast.LENGTH_SHORT).show()

            }
        })
        postEditText.setText("")

    }
    //For twitter when user types the character count needs to be changed
    fun onTextChangeListener(){
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

    fun sendToMainActivity(){
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