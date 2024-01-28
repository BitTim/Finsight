package dev.bittim.finsight.screens.signin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.finsight.ui.theme.FinsightTheme

@Composable
fun SignInScreen(signInViewModel: SignInViewModel? = null,
                 onNavToHomePage:() -> Unit,
                 onNavToRegisterPage:() -> Unit) {
    val signInUiState = signInViewModel?.signInUiState;
    val isError = signInUiState?.signInError != null;
    val context = LocalContext.current;
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Sign In",
            style = MaterialTheme.typography.headlineLarge
        );
        
        if(isError) {
            Text(
                text = signInUiState?.signInError ?: "Something went wrong",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            );
        }
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = signInUiState?.emailSignIn.orEmpty(),
                onValueChange = {signInViewModel?.onEmailSignInChange(it)},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email"
                    )
                },
                label = {
                    Text(
                        text = "Email",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                isError = isError
            );

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                value = signInUiState?.passwordSignIn.orEmpty(),
                onValueChange = {signInViewModel?.onPasswordSignInChange(it)},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password"
                    )
                },
                label = {
                    Text(
                        text = "Password",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                isError = isError
            );
            
            Button(
                onClick = { signInViewModel?.signIn(context) }
            ) {
                Text(text = "Sign In"); 
            }
            
            Spacer(modifier = Modifier.padding(16.dp))
            
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Text(text = "Don't have an account?");
                Spacer(modifier = Modifier.padding(4.dp));
                TextButton(onClick = { onNavToRegisterPage.invoke() }) {
                    Text(text = "Register");
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SignInScreenPreview() {
    FinsightTheme {
        SignInScreen(
            onNavToHomePage = {},
            onNavToRegisterPage = {}
        );
    }
}