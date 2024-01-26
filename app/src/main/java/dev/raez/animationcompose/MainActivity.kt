package dev.raez.animationcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.raez.animationcompose.ui.theme.AnimationComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Login()
        }
    }
}

@Composable
fun Login() {
    var user by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var validationMessage by remember { mutableStateOf("") }
    var passVisible by remember { mutableStateOf(false) }
    val isLoginEnable = user.isNotEmpty() && password.isNotEmpty()
    val isError = validationMessage.isNotEmpty()
    val login = { validationMessage = validateLogin(user, password) }

    Screen {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        ) {

            TextField(
                value = user,
                onValueChange = { user = it },
                isError = isError,
                singleLine = true,
                //keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email,imeAction = ImeAction.Next)
            )
            TextField(
                value = password,
                onValueChange = { password = it },
                isError = isError,
                singleLine = true,
                visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation(),
                // keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconToggleButton(
                        checked = passVisible,
                        onCheckedChange = { passVisible = it }) {
                        Icon(
                            imageVector = if (!passVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Visibility"
                        )
                    }
                }

            )

            AnimatedVisibility(
                visible = validationMessage.isNotEmpty(),
                enter = slideInHorizontally(animationSpec = tween(durationMillis = 200)) { fullWidth ->
                    // Offsets the content by 1/3 of its width to the left, and slide towards right
                    // Overwrites the default animation with tween for this slide animation.
                    -fullWidth / 3
                } + fadeIn(
                    // Overwrites the default animation with tween
                    animationSpec = tween(durationMillis = 200)
                ),
                exit = slideOutHorizontally(animationSpec = spring(stiffness = Spring.StiffnessHigh)) {
                    // Overwrites the ending position of the slide-out to 200 (pixels) to the right
                    200
                } + fadeOut()
            ) {
                Text(text = validationMessage, color = MaterialTheme.colorScheme.error)
            }

            AnimatedVisibility(
                visible = isLoginEnable,
                enter = slideInHorizontally(animationSpec = tween(durationMillis = 1000)) { fullWidth ->
                    // Offsets the content by 1/3 of its width to the left, and slide towards right
                    // Overwrites the default animation with tween for this slide animation.
                    -fullWidth / 3
                } + fadeIn(
                    // Overwrites the default animation with tween
                    animationSpec = tween(durationMillis = 200)
                ),
                exit = slideOutHorizontally(animationSpec = tween(durationMillis = 1000)) {
                    // Overwrites the ending position of the slide-out to 200 (pixels) to the right
                    -it * 3
                } + fadeOut()
            ) {
                Button(
                    onClick = login
                ) {
                    Text(text = "Login")
                }
            }
        }

    }
}

fun validateLogin(user: String, password: String): String {
    return when {
        !user.contains('@') -> "User must contain @"
        password.length < 5 -> "Password must have at least 5 characters"
        else -> ""
    }
}

@Composable
fun Screen(composable: @Composable () -> Unit) {
    AnimationComposeTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            composable()
        }
    }
}

@Preview(showBackground = true, device = Devices.DEFAULT)
@Composable
fun LoginPreview() {
    Login()
}