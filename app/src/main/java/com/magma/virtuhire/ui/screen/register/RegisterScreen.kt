package com.magma.virtuhire.ui.screen.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.magma.virtuhire.R
import com.magma.virtuhire.ui.components.common.ErrorDialog
import com.magma.virtuhire.ui.components.common.InputField
import com.magma.virtuhire.ui.components.common.LoadingDialog
import com.magma.virtuhire.ui.components.common.PrimaryButton
import com.magma.virtuhire.ui.screen.onboard.BottomSection
import com.magma.virtuhire.ui.screen.onboard.OnBoardingItem
import com.magma.virtuhire.ui.screen.onboard.OnboardScreen
import com.magma.virtuhire.ui.theme.Primary500
import com.magma.virtuhire.ui.theme.Primary700
import com.magma.virtuhire.ui.theme.PrimaryGray100
import com.magma.virtuhire.ui.theme.PrimaryGray1200
import com.magma.virtuhire.ui.theme.VirtuHireTheme
import com.magma.virtuhire.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun RegisterScreen(
    navigateToLogin: () -> Unit,
    loginViewModel: RegisterViewModel = hiltViewModel()
) {
    val state = loginViewModel.registerState.value

    var isLoadingDialogExpanded by remember { mutableStateOf(false) }
    var isErrorDialogExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state) {
        isLoadingDialogExpanded = state.isLoading
        if (state.isError) {
            isErrorDialogExpanded = true
        }
        if (state.userData != null) {
            showToast(context, "Register Success")
            navigateToLogin()
        }
    }

    RegisterContent(onRegisterButtonClick = { firstName, lastName, email, password ->
        coroutineScope.launch {
            try {
                loginViewModel.registerUser(
                    firstName,
                    lastName,
                    email,
                    password,
                    passwordConfirmation = password
                )
            } catch (e: Exception) {
                // Handle exceptions if necessary
            }
        }
    }, onLoginTextClick = { navigateToLogin() })

    LoadingDialog(isDialogOpen = isLoadingDialogExpanded) {
        isLoadingDialogExpanded = false
    }
    ErrorDialog(
        isDialogOpen = isErrorDialogExpanded,
        onDialogClose = { isErrorDialogExpanded = false },
        errorMessage = state.errorMessage ?: "There is an error"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterContent(
    onRegisterButtonClick: (firstName: String, lastName: String, email: String, password: String) -> Unit,
    onLoginTextClick: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val annotatedText = buildAnnotatedString {
        append("Already have an account? ")
        pushStringAnnotation(
            tag = "Login",
            annotation = "Navigate to login screen"
        )
        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
            append("Login")
        }
        pop()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Primary500, Primary700
                    ),
                    startY = 0f,
                )
            )
    ) {
        Text(
            text = stringResource(R.string.app_name_lowercase),
            style = MaterialTheme.typography.displaySmall,
            color = PrimaryGray100,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp)
        )

        Box(
            modifier = Modifier
                .shadow(
                    elevation = 50.dp,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    clip = true
                )
                .background(color = PrimaryGray100)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = 55.dp, start = 20.dp, end = 20.dp, bottom = 40.dp)
            ) {
                Column {
                    InputField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = stringResource(R.string.user_first_name_label),
//                        validation = { input ->
//                            val isValid = input.isNotEmpty()
//                            val errorMessage = when {
//                                input.isEmpty() -> "Firstname is required"
//                                else -> ""
//                            }
//                            isValid to errorMessage
//                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    InputField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = stringResource(R.string.user_last_name_label),
//                        validation = { input ->
//                            val isValid = input.isNotEmpty()
//                            val errorMessage = when {
//                                input.isEmpty() -> "Lastname is required"
//                                else -> ""
//                            }
//                            isValid to errorMessage
//                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    InputField(
                        value = email,
                        onValueChange = { email = it },
                        label = stringResource(R.string.user_email_label),
//                        validation = { input ->
//                            val emailRegex =
//                                Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
//                            val isValid = input.isNotEmpty() && input.matches(emailRegex)
//                            val errorMessage = when {
//                                input.isEmpty() -> "Email is required"
//                                !input.matches(emailRegex) -> "Invalid email format"
//                                else -> ""
//                            }
//                            isValid to errorMessage
//                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    InputField(
                        value = password,
                        onValueChange = { password = it },
                        label = stringResource(R.string.user_password_label),
//                        validation = { input ->
//                            val isValid = input.isNotEmpty() && input.length >= 8
//                            val errorMessage = when {
//                                input.isEmpty() -> "Password is required"
//                                input.length < 8 -> "Password must be at least 8 characters"
//                                else -> ""
//                            }
//                            isValid to errorMessage
//                        },
                        isPassword = true
                    )
                }

                Column {
                    PrimaryButton(
                        text = stringResource(R.string.register_button),
                        onClick = { onRegisterButtonClick(firstName, lastName, email, password) },
                        modifier = Modifier.height(72.dp)
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    ClickableText(
                        text = annotatedText,
                        onClick = { offset ->
                            annotatedText.getStringAnnotations(
                                tag = "Login",
                                start = offset,
                                end = offset
                            ).firstOrNull()?.let { annotation ->
                                if (annotation.item == "Navigate to login screen") {
                                    onLoginTextClick()
                                }
                            }
                        },
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Primary700,
                            textAlign = TextAlign.Center,
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    VirtuHireTheme {
        RegisterContent(onRegisterButtonClick = { _, _, _, _ -> }, onLoginTextClick = {})
    }
}

