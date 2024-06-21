package com.dicoding.kulitku.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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

// LOGIN GOOGLE
data class AuthResponse(
    @SerializedName("message") val message: String,
    @SerializedName("id_user") val id_user: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("address") val address: String
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

data class DiseaseData(
    val nama_penyakit: String,
    val obat_rekomendasi: String
)

data class Product(
    val id_produk: Int,
    val nama_produk: String,
    val kategori_produk: String,
    val jenis_kulit: String,
    val manfaat_utama: String,
    val cara_penggunaan: String,
    val gambar_produk: String,
)

// ARTIKEL
@Parcelize
data class ResponseArticleItem(
    @SerializedName("id") val id: Int,
    @SerializedName("image_url") val image_url: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
) : Parcelable

@Parcelize
data class ResponseQuizItem(
    @SerializedName("id") val id: Int,
    @SerializedName("question") val question: String,
    @SerializedName("optionA") val optionA: String,
    @SerializedName("optionB") val optionB: String,
    @SerializedName("optionC") val optionC: String,
    @SerializedName("optionD") val optionD: String,
    @SerializedName("answer") val answer: String
) : Parcelable

// TIPS
@Parcelize
data class ResponseTipsItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
) : Parcelable