package com.dicoding.kulitku.api

import android.provider.ContactsContract.CommonDataKinds.Email
import com.google.gson.annotations.SerializedName

data class RegisterData(
    var name: String,
    var email: String,
    var password: String
)

data class LoginData(
    var email: String,
    var password: String
)

// USER
data class UserData(
    @SerializedName("id_user") val idUser: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

// REGISTER
data class RegisterResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: UserData
)

// LOGIN
data class LoginResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("userId") val userId: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("message") val message: String
)

// PROFILE
data class GetProfileResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("userProfile") val userProfile: UserProfile
)

data class UserProfile(
    @SerializedName("data") val data: List<UserData>
)


// UPDATE PROFILE
data class UpdateProfileResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("id_user") val idUser: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

// DELETE PROFILE
data class DeleteProfileResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String
)