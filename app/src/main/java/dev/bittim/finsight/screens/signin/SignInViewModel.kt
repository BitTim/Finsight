package dev.bittim.finsight.screens.signin

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bittim.finsight.repos.AuthRepo
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.IllegalArgumentException

class SignInViewModel(
    private val authRepo: AuthRepo = AuthRepo()
) : ViewModel() {
    val user = authRepo.currentUser;
    val hasUser:Boolean get() = authRepo.hasUser();
    
    var signInUiState by mutableStateOf(SignInUiState())
        private set

    fun onEmailSignInChange(email:String) {
        signInUiState = signInUiState.copy(emailSignIn = email);
    }
    fun onPasswordSignInChange(password:String) {
        signInUiState = signInUiState.copy(passwordSignIn = password);
    }
    fun onUsernameRegisterChange(username:String) {
        signInUiState = signInUiState.copy(usernameRegister = username);
    }
    fun onEmailRegisterChange(email:String) {
        signInUiState = signInUiState.copy(emailRegister = email);
    }
    fun onPasswordRegisterChange(password:String) {
        signInUiState = signInUiState.copy(passwordRegister = password);
    }
    fun onPasswordConfirmRegisterChange(passwordConfirm:String) {
        signInUiState = signInUiState.copy(passwordConfirmRegister = passwordConfirm);
    }
    
    
    
    private fun validateSignInForm() = signInUiState.emailSignIn.isNotBlank()
            && signInUiState.passwordSignIn.isNotBlank();
    
    private fun validateRegisterForm() = signInUiState.usernameRegister.isNotBlank()
            && signInUiState.emailRegister.isNotBlank()
            && signInUiState.passwordRegister.isNotBlank()
            && signInUiState.passwordConfirmRegister.isNotBlank()
            && signInUiState.passwordRegister == signInUiState.passwordConfirmRegister;
    
    private fun getSignInFormError():String {
        if(signInUiState.emailSignIn.isBlank()) return "E-Mail cannot be empty!";
        if(signInUiState.passwordSignIn.isBlank()) return "Password cannot be empty!";
        return "";
    }
    
    private fun getRegisterFormError():String {
        if(signInUiState.usernameRegister.isBlank()) return "Username cannot be empty!";
        if(signInUiState.emailRegister.isBlank()) return "E-Mail cannot be empty!";
        if(signInUiState.passwordRegister.isBlank()) return "Password cannot be empty!";
        if(signInUiState.passwordConfirmRegister.isBlank()) return "Password confirmation cannot be empty!";
        if(signInUiState.passwordRegister != signInUiState.passwordConfirmRegister) return "Passwords must match!";
        return "";
    }
    
    
    
    fun signIn(context: Context) = viewModelScope.launch { 
        try {
            if(!validateSignInForm()) throw IllegalArgumentException(getSignInFormError());
            signInUiState = signInUiState.copy(isLoading = true, signInError = null);
            
            authRepo.signIn(signInUiState.emailSignIn, signInUiState.passwordSignIn) { success ->
                signInUiState = signInUiState.copy(isSignInSuccess = success);
                if(success) {
                    Toast.makeText(context, "Signed in", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to sign in", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (e:Exception) {
            signInUiState = signInUiState.copy(signInError = e.localizedMessage);
            e.printStackTrace();
        } finally {
            signInUiState = signInUiState.copy(isLoading = false);
        }
    }
    
    fun createUser(context: Context) = viewModelScope.launch { 
        try {
            if(!validateRegisterForm()) throw IllegalArgumentException(getRegisterFormError());
            signInUiState = signInUiState.copy(isLoading = true, registerError = null);
            
            authRepo.createUser(signInUiState.emailRegister, signInUiState.passwordRegister) { success ->
                signInUiState = signInUiState.copy(isSignInSuccess = success);
                if(success) {
                    Toast.makeText(context, "User created", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to create user", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (e:Exception) {
            signInUiState = signInUiState.copy(registerError = e.localizedMessage);
            e.printStackTrace();
        } finally {
            signInUiState = signInUiState.copy(isLoading = false);
        }
    }
}



data class SignInUiState(
    val emailSignIn: String = "",
    val passwordSignIn: String = "",
    val usernameRegister: String = "",
    val emailRegister: String = "",
    val passwordRegister: String = "",
    val passwordConfirmRegister: String = "",
    
    val isLoading: Boolean = false,
    val isSignInSuccess: Boolean = false,
    val signInError: String? = null,
    val registerError: String? = null,
)