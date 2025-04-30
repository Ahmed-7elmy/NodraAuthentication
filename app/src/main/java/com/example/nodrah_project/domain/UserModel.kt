package com.example.nodrah_project.domain

import com.google.firebase.auth.FirebaseUser

data class UserModel(
    val email: String,
    val isEmailVerified: Boolean = false,
    val creationDate: String? = null
) {
    companion object {
        fun fromFirebaseUser(user: FirebaseUser?): UserModel? {
            return user?.let {
                UserModel(
                    email = it.email ?: "",
                    isEmailVerified = it.isEmailVerified
                )
            }
        }
    }
}
