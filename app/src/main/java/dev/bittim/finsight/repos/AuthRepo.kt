package dev.bittim.finsight.repos

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepo {
    val currentUser:FirebaseUser? = Firebase.auth.currentUser;
    
    fun hasUser():Boolean {
        return Firebase.auth.currentUser != null;
    }
    
    fun getUid():String {
        return Firebase.auth.currentUser?.uid.orEmpty();
    }
    
    suspend fun createUser(email:String, password:String, onComplete:(Boolean) -> Unit): AuthResult = withContext(Dispatchers.IO) {
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { 
                onComplete.invoke(it.isSuccessful);
            }.await()
    }
    
    suspend fun signIn(email:String, password:String, onComplete:(Boolean) -> Unit): AuthResult = withContext(Dispatchers.IO) {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { 
                onComplete.invoke(it.isSuccessful);
            }.await()
    }
}