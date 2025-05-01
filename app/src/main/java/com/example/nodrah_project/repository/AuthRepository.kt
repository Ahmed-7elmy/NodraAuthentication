package com.example.nodrah_project.repository

import com.example.nodrah_project.domain.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import kotlinx.coroutines.tasks.await

sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}

class AuthRepository(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) {

    suspend fun login(email: String, password: String): AuthResult {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun signUp(email: String, password: String): AuthResult {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun sendPasswordReset(email: String): AuthResult {
        return try {
            auth.sendPasswordResetEmail(email).await()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Unknown error")
        }
    }
    suspend fun getCurrentUserModel(): UserModel? {
        return UserModel.fromFirebaseUser(auth.currentUser)
    }

//    suspend fun reloadUser(): AuthResult {
//        return try {
//            auth.currentUser?.reload()?.await()
//            AuthResult.Success
//        } catch (e: Exception) {
//            AuthResult.Error(e.message ?: "Failed to refresh user")
//        }
//    }
    suspend fun sendVerificationEmail(): AuthResult {
        return try {
            auth.currentUser?.sendEmailVerification()?.await()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Failed to send verification email")
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUserEmail(): String? {
        return auth.currentUser?.email
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
    suspend fun signInWithPhoneCredential(credential: PhoneAuthCredential): AuthResult {
        return try {
            auth.signInWithCredential(credential).await()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Phone verification failed")
        }
    }
    fun isPhoneUser(): Boolean {
        return auth.currentUser?.phoneNumber != null
    }
}