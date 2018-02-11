package com.kodesnippets.sharetastic

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.*



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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        setUpViews()

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




        }
    }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_logout,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_logout -> {

                return true
            }
            else -> return super.onOptionsItemSelected(item)

        }
    }




}