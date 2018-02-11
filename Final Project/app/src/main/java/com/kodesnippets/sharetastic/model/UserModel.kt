package com.kodesnippets.sharetastic.model

import java.io.Serializable

/**
 * Created by aaqibhussain on 10/2/18.
 */


class UserModel(val name: String, val userName: String, val profilePictureUrl: String, val socialNetwork: SocialNetwork) : Serializable

enum class SocialNetwork {
    Facebook, Twitter
}